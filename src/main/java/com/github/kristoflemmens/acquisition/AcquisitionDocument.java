package com.github.kristoflemmens.acquisition;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

public class AcquisitionDocument {
    private final UUID id;

    public AcquisitionDocument(UUID id) {
        this.id = id;
    }

    public UUID id() {
        return id;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        AcquisitionDocument that = (AcquisitionDocument) obj;

        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
