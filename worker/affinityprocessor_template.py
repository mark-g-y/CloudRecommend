
@outputSchema("(user:chararray, item:chararray, score:double)")
def add_affinity(user, item, event, timee):  
    event_to_score = {{ event_to_score_map }}
    score = event_to_score.get(event)

    return user, item, adjust_score_by_time(score, timee) if score is not None else 0

def adjust_score_by_time(score, timee):
    current_time = {{ current_time }}
    time_max_limits = {{ time_max_limits }}
    adjustments = {{ adjustments }}

    for i in range(0, len(time_max_limits)):
        if current_time - timee < time_max_limits:
            return score * adjustments[i]

    return score * adjustments[-1]
