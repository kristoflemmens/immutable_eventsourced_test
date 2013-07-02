package com.github.kristoflemmens.acquisition;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.UUID;

import static com.github.kristoflemmens.acquisition.Event.*;
import static com.github.kristoflemmens.acquisition.Request.loadFromHistory;
import static java.util.UUID.randomUUID;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.util.Lists.newArrayList;

public class RequestTest {
    private static final UUID ID = randomUUID();
    private static final AcquisitionDocument ACQUISITION_DOCUMENT = new AcquisitionDocument(ID, "");
    private static final AcquisitionDocument ACQUISITION_DOCUMENT_WITH_ACTOR = new AcquisitionDocument(ID, "myActor");

    @Test
    public void receive() throws Exception {
        ReceivedRequest request = Request.receive(ACQUISITION_DOCUMENT);

        assertThat(request.uncommittedEvents()).containsExactly(new RequestReceived(ID, ACQUISITION_DOCUMENT));
    }

    @Test
    public void addActor() throws Exception {
        ReceivedRequest request = Request.receive(ACQUISITION_DOCUMENT).addActor("myActor");

        assertThat(request.uncommittedEvents()).containsExactly(new RequestReceived(ID, ACQUISITION_DOCUMENT), new ActorAdded(ID, ACQUISITION_DOCUMENT_WITH_ACTOR));
    }

    @Test
    public void addActor_LoadFromHistory() throws Exception {
        ReceivedRequest request = loadFromHistory(Lists.<Event>newArrayList(new RequestReceived(ID, ACQUISITION_DOCUMENT)));

        ReceivedRequest actual = request.addActor("myActor");

        assertThat(actual.uncommittedEvents()).containsExactly(new ActorAdded(ID, ACQUISITION_DOCUMENT_WITH_ACTOR));
    }

    @Test
    public void validate_LoadFromHistory() throws Exception {
        ReceivedRequest request = loadFromHistory(Lists.<Event>newArrayList(new RequestReceived(ID, ACQUISITION_DOCUMENT)));

        ValidatedRequest actual = request.validate();

        assertThat(actual.uncommittedEvents()).containsExactly(new RequestValidated(ID, ACQUISITION_DOCUMENT));
    }

    @Test
    public void validate() throws Exception {
        ValidatedRequest actual = Request.receive(ACQUISITION_DOCUMENT).validate();

        assertThat(actual.uncommittedEvents()).containsExactly(new RequestReceived(ID, ACQUISITION_DOCUMENT), new RequestValidated(ID, ACQUISITION_DOCUMENT));
    }

    @Test
    public void acquire_LoadFromHistory() throws Exception {
        ValidatedRequest request = loadFromHistory(newArrayList(new RequestReceived(ID, ACQUISITION_DOCUMENT), new RequestValidated(ID, ACQUISITION_DOCUMENT)));

        AcquiredRequest actual = request.acquire();

        assertThat(actual.uncommittedEvents()).containsExactly(new RequestAcquired(ID, ACQUISITION_DOCUMENT));
    }

    @Test
    public void acquire() throws Exception {
        AcquiredRequest actual = Request.receive(ACQUISITION_DOCUMENT).validate().acquire();

        assertThat(actual.uncommittedEvents()).containsExactly(
                new RequestReceived(ID, ACQUISITION_DOCUMENT),
                new RequestValidated(ID, ACQUISITION_DOCUMENT),
                new RequestAcquired(ID, ACQUISITION_DOCUMENT));
    }
}
