package com.github.kristoflemmens.acquisition;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

class AcquiredRequest extends Request {
    AcquiredRequest(UUID id, AcquisitionDocument acquisitionDocument, List<Event> uncommittedEvents) {
        super(id, acquisitionDocument, uncommittedEvents);
    }

    @Override
    protected AcquiredRequest markCommitted() {
        return new AcquiredRequest(id(), acquisitionDocument(), Lists.<Event>newArrayList());
    }

    @Override
    protected Request apply(Event event) {
        return unhandled(event);
    }
}
