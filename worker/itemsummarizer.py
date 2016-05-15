
@outputSchema("(user:chararray, items:chararray)")
def merge_uii_rec(user, items):
    itemslist = []
    for item in items:
        itemslist.append(item[2])
    return user[0][0], ','.join(itemslist)

@outputSchema("(item:chararray, items:chararray)")
def merge_itoi_rec(item, items):
    itemslist = []
    for item in items:
        itemslist.append(item[1])
    return item[0], ','.join(itemslist)
