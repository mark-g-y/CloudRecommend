
@outputSchema("(user:chararray, items:chararray)")
def merge_uii_rec(user, items, top_score):
    itemslist = []
    for item in items:
        itemslist.append({'item':item[2].encode('ascii', 'ignore'), 'score':item[3] / top_score})
    return user[0][0], str(itemslist)


@outputSchema("(item:chararray, items:chararray)")
def merge_itoi_rec(item, items, top_score):
    itemslist = []
    for item in items:
        itemslist.append({'item':item[1].encode('ascii', 'ignore'), 'score':item[2] / top_score})
    return item[0], str(itemslist)
