package com.github.kristoflemmens.acquisition;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.UUID;

import static com.github.kristoflemmens.acquisition.Request.*;
import static java.util.UUID.randomUUID;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.util.Lists.newArrayList;

public class RequestTest {
    private static final UUID ID = randomUUID();
    private static final AcquisitionDocument ACQUISITION_DOCUMENT = new AcquisitionDocument(ID);

    @Test
    public void constructor() throws Exception {
        Request request = receive(ACQUISITION_DOCUMENT);

        assertThat(request.uncommittedEvents()).containsExactly(new Event.RequestReceived(ID, ACQUISITION_DOCUMENT));
    }

    @Test
    public void validate_LoadFromHistory() throws Exception {
        ReceivedRequest request = loadFromHistory(Lists.<Event>newArrayList(new Event.RequestReceived(ID, ACQUISITION_DOCUMENT)));

        ValidatedRequest actual = request.validate();

        assertThat(actual.uncommittedEvents()).containsExactly(new Event.RequestValidated(ID, ACQUISITION_DOCUMENT));
    }

    @Test
    public void validate() throws Exception {
        ValidatedRequest actual = receive(ACQUISITION_DOCUMENT).validate();

        assertThat(actual.uncommittedEvents()).containsExactly(new Event.RequestReceived(ID, ACQUISITION_DOCUMENT), new Event.RequestValidated(ID, ACQUISITION_DOCUMENT));
    }

    @Test
    public void acquire_LoadFromHistory() throws Exception {
        ValidatedRequest request = loadFromHistory(newArrayList(new Event.RequestReceived(ID, ACQUISITION_DOCUMENT), new Event.RequestValidated(ID, ACQUISITION_DOCUMENT)));

        AcquiredRequest actual = request.acquire();

        assertThat(actual.uncommittedEvents()).containsExactly(new Event.RequestAcquired(ID, ACQUISITION_DOCUMENT));
    }

    @Test
    public void acquire() throws Exception {
        AcquiredRequest actual = receive(ACQUISITION_DOCUMENT).validate().acquire();

        assertThat(actual.uncommittedEvents()).containsExactly(
                new Event.RequestReceived(ID, ACQUISITION_DOCUMENT),
                new Event.RequestValidated(ID, ACQUISITION_DOCUMENT),
                new Event.RequestAcquired(ID, ACQUISITION_DOCUMENT));
    }
}
