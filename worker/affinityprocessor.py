
@outputSchema("(user:chararray, item:chararray, score:double)")
def add_affinity(user, item, event, timee):  
    event_to_score = {u'purchase': 1.0, u'like': 0.3}
    score = event_to_score.get(event)

    return user, item, adjust_score_by_time(score, timee) if score is not None else 0

def adjust_score_by_time(score, timee):
    current_time = 1463707003965
    time_max_limits = [86400000L, 604800000L, 2629746000L, 31556952000L]
    adjustments = [1.5, 1.0, 0.5, 0.25]

    for i in range(0, len(time_max_limits)):
        if current_time - timee < time_max_limits:
            return score * adjustments[i]

    return score * adjustments[-1]