package com.github.kristoflemmens.acquisition;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.skip;
import static com.google.common.collect.Lists.newArrayList;

abstract class Request {

    private final UUID id;
    private final AcquisitionDocument acquisitionDocument;
    private final List<Event> uncommittedEvents;

    Request(UUID id, AcquisitionDocument acquisitionDocument, List<Event> uncommittedEvents) {
        this.id = id;
        this.acquisitionDocument = acquisitionDocument;
        this.uncommittedEvents = uncommittedEvents;
    }

    static Request unhandled(Event event) {
        throw new UnsupportedOperationException("Could not handle event " + event);
    }

    static ReceivedRequest receive(AcquisitionDocument acquisitionDocument) {
        return applyReceived(new Event.RequestReceived(acquisitionDocument.id(), acquisitionDocument));
    }

    private static ReceivedRequest applyReceived(Event.RequestReceived requestReceived) {
        return new ReceivedRequest(requestReceived.id(), requestReceived.acquisitionDocument(), Lists.<Event>newArrayList(requestReceived));
    }

    private static Request init(Event event) {
        if (event instanceof Event.RequestReceived) {
            return applyReceived((Event.RequestReceived) event);
        } else {
            return unhandled(event);
        }
    }

    static <R extends Request> R loadFromHistory(List<Event> events) {
        Request aggregate = init(getFirst(events, null));
        for (Event event : skip(events, 1)) aggregate = aggregate.apply(event);
        //noinspection unchecked
        return (R) aggregate.markCommitted();
    }

    protected abstract Request apply(Event event);

    protected abstract Request markCommitted();

    List<Event> uncommittedEvents() {
        return ImmutableList.copyOf(uncommittedEvents);
    }

    UUID id() {
        return id;
    }

    AcquisitionDocument acquisitionDocument() {
        return acquisitionDocument;
    }

    protected List<Event> uncommittedEventsWith(Event event) {
        List<Event> uncommittedEvents = newArrayList(uncommittedEvents());
        uncommittedEvents.add(event);
        return uncommittedEvents;
    }
}
