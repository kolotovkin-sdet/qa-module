package com.qa.features;

import com.qa.atlibs.grpc.manager.GrpcManager;
import com.qa.atlibs.grpc.steps.GrpcSteps;
import hello.Hello;
import hello.HelloServiceGrpc;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Tag("qa-module")
public class GrpcTests {

    HelloServiceGrpc.HelloServiceBlockingStub stub
            = HelloServiceGrpc.newBlockingStub(GrpcManager.getGrpcChannel("postman-grpcb-in"));
    HelloServiceGrpc.HelloServiceStub asyncStub
            = HelloServiceGrpc.newStub(GrpcManager.getGrpcChannel("postman-grpcb-in"));

    @ParameterizedTest
    @ValueSource(strings = {"Ilya", "Billy"})
    void demoTest_unaryRequest(String name) {
        GrpcSteps.prepareRequest(Hello.HelloRequest.class)
                .jsonBodyForRequestIs("unary-request-body.json")
                .metadataForRequestAre(Map.of("testKey", "testValue"))
                .sendGrpcUnaryRequest(stub::sayHello)
                .jsonResponseIs("unary-response-body.json")
                .responseMetadataIs(Map.of(
                        "content-type", "application/grpc"
                ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ilya", "Billy"})
    void demoTest_ServiceStreamingRequest(String name) {
        GrpcSteps.<Hello.HelloRequest, Hello.HelloResponse>prepareRequest(Hello.HelloRequest.class)
                .jsonBodyForRequestIs("ss-request-body.json")
                .metadataForRequestAre(Map.of("testKey", "testValue"))
                .sendGrpcServiceStreamingRequest(stub::lotsOfReplies)
                .streamingResponseQtyIs(10)
                .jsonResponsesAre(Collections.nCopies(10, "ss-response-body.json"))
                .responseMetadataIs(Map.of(
                        "content-type", "application/grpc"
                ));
    }

    @ParameterizedTest
    @CsvSource({"Ilya,Kolotovkin", "Billy,Harrington"})
    void demoTest_ClientStreaming(String nameOne, String nameTwo) {
        GrpcSteps.<Hello.HelloRequest, Hello.HelloResponse>prepareRequest(Hello.HelloRequest.class)
                .jsonBodiesForRequestAre(List.of(
                        "cs-request-body-1.json",
                        "cs-request-body-2.json"))
                .metadataForRequestAre(Map.of("testKey", "testValue"))
                .sendGrpcClientStreamingRequest(asyncStub::lotsOfGreetings)
                .jsonResponseIs("cs-response-body.json")
                .responseMetadataIs(Map.of(
                        "content-type", "application/grpc"
                ));
    }

    @ParameterizedTest
    @CsvSource({"Ilya,Kolotovkin", "Billy,Harrington"})
    void demoTest_BidirectionalStreaming(String nameOne, String nameTwo) {
        GrpcSteps.<Hello.HelloRequest, Hello.HelloResponse>prepareRequest(Hello.HelloRequest.class)
                .jsonBodiesForRequestAre(List.of(
                        "bds-request-body-1.json",
                        "bds-request-body-2.json"))
                .metadataForRequestAre(Map.of("testKey", "testValue"))
                .sendGrpcBidirectionalStreamingRequest(asyncStub::bidiHello)
                .streamingResponseQtyIs(2)
                .jsonResponsesAre(List.of(
                        "bds-response-body-1.json",
                        "bds-response-body-2.json"))
                .responseMetadataIs(Map.of(
                        "content-type", "application/grpc"
                ));
    }

}
