package com.github.kristoflemmens.acquisition;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

class ValidatedRequest extends Request {

    ValidatedRequest(UUID id, AcquisitionDocument acquisitionDocument, List<Event> uncommittedEvents) {
        super(id, acquisitionDocument, uncommittedEvents);
    }

    @Override
    protected ValidatedRequest markCommitted() {
        return new ValidatedRequest(id(), acquisitionDocument(), Lists.<Event>newArrayList());
    }

    @Override
    protected Request apply(Event event) {
        if (event instanceof Event.RequestAcquired) {
            return applyAcquired((Event.RequestAcquired) event);
        } else {
            return unhandled(event);
        }
    }

    private AcquiredRequest applyAcquired(Event.RequestAcquired requestAcquired) {
        return new AcquiredRequest(requestAcquired.id(), requestAcquired.acquisitionDocument(), uncommittedEventsWith(requestAcquired));
    }

    AcquiredRequest acquire() {
        return applyAcquired(new Event.RequestAcquired(id(), acquisitionDocument()));
    }

}
