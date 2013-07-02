package com.github.kristoflemmens.acquisition;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

abstract class Event {
    private final UUID id;
    private final AcquisitionDocument acquisitionDocument;

    Event(UUID id, AcquisitionDocument acquisitionDocument) {
        this.id = id;
        this.acquisitionDocument = acquisitionDocument;
    }

    public UUID id() {
        return id;
    }

    public AcquisitionDocument acquisitionDocument() {
        return acquisitionDocument;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Event that = (Event) obj;

        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class RequestReceived extends Event {

        RequestReceived(UUID id, AcquisitionDocument acquisitionDocument) {
            super(id, acquisitionDocument);
        }
    }

    public static class RequestValidated extends Event {

        RequestValidated(UUID id, AcquisitionDocument acquisitionDocument) {
            super(id, acquisitionDocument);
        }
    }

    public static class RequestAcquired extends Event {

        RequestAcquired(UUID id, AcquisitionDocument acquisitionDocument) {
            super(id, acquisitionDocument);
        }
    }

    public static class ActorAdded extends Event {

        ActorAdded(UUID id, AcquisitionDocument acquisitionDocument) {
            super(id, acquisitionDocument);
        }
    }
}
