package com.github.kristoflemmens.acquisition;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

class ReceivedRequest extends Request {
    ReceivedRequest(UUID id, AcquisitionDocument acquisitionDocument, List<Event> uncommittedEvents) {
        super(id, acquisitionDocument, uncommittedEvents);
    }

    @Override
    protected ReceivedRequest markCommitted() {
        return new ReceivedRequest(id(), acquisitionDocument(), Lists.<Event>newArrayList());
    }

    ValidatedRequest validate() {
        return applyValidated(new Event.RequestValidated(id(), acquisitionDocument()));
    }

    @Override
    protected Request apply(Event event) {
        if (event instanceof Event.RequestValidated) {
            return applyValidated((Event.RequestValidated) event);
        } else if (event instanceof Event.ActorAdded) {
            return applyActorAdded((Event.ActorAdded) event);
        } else {
            return unhandled(event);
        }
    }

    private ValidatedRequest applyValidated(Event.RequestValidated requestValidated) {
        return new ValidatedRequest(requestValidated.id(), requestValidated.acquisitionDocument(), uncommittedEventsWith(requestValidated));
    }

    public ReceivedRequest addActor(String myActor) {
        return applyActorAdded(new Event.ActorAdded(id(), new AcquisitionDocument(acquisitionDocument().id(), myActor)));
    }

    private ReceivedRequest applyActorAdded(Event.ActorAdded actorAdded) {
        return new ReceivedRequest(actorAdded.id(), actorAdded.acquisitionDocument(), uncommittedEventsWith(actorAdded));
    }

}
