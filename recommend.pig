
register 'affinityprocessor.py' using jython as affinity;

inp = load '$filename' using PigStorage(' ') as (user: chararray, item: chararray, event: chararray, time: float);

inpaff = foreach inp {
    fields = affinity.add_affinity(user, item, event, time);
    generate fields.user, fields.item, fields.score;
}
inpaff = group inpaff by (user, item);
u_to_i = foreach inpaff generate group.user as user, group.item as item, SUM(inpaff.score) as score;
u_to_i2 = foreach u_to_i generate *;

i_to_i = join u_to_i by user, u_to_i2 by user;
i_to_i = filter i_to_i by u_to_i::item != u_to_i2::item;
i_to_i = foreach i_to_i generate u_to_i::item as item1, u_to_i2::item as item2, (u_to_i::score < u_to_i2::score ? u_to_i::score : u_to_i2::score) as score;
i_to_i = group i_to_i by (item1, item2);
i_to_i = foreach i_to_i generate group.item1 as item1, group.item2 as item2, SUM(i_to_i.score) as score;

uii = join u_to_i by item, i_to_i by item1;
uii = foreach uii generate u_to_i::user as user, i_to_i::item1 as item1, i_to_i::item2 as item2, (u_to_i::score + i_to_i::score) as score;
uii = group uii by user;
uii = foreach uii {
	sorted_score = order uii by score desc;
	sorted_score = limit sorted_score 2;
	generate uii.user, uii.item1, uii.item2, flatten(sorted_score);
}
uii = foreach uii generate user, item1, item2, score;

dump uii;
