
@outputSchema("(user:chararray, item:chararray, score:double)")
def add_affinity(user, item, event, timee):  
    event_to_score = {'purchase': 1.0, 'like': 0.5, 'view': 0.1}
    score = event_to_score.get(event)

    return user, item, adjust_score_by_time(score, timee) if score is not None else 0

def adjust_score_by_time(score, timee):
    current_time = 1462997841322
    time_max_limits = [86400000L, 604800000L, 2629746000L, 31556952000L]
    adjustments = [1.5, 1.0, 0.5, 0.25]

    for i in range(0, len(time_max_limits)):
        if current_time - timee < time_max_limits:
            return score * adjustments[i]

    return score * adjustments[-1]