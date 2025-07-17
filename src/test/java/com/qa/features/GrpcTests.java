package com.qa.features;

import com.google.protobuf.InvalidProtocolBufferException;

import com.google.protobuf.Message;
import com.qa.atlibs.grpc.manager.GrpcManager;
import com.qa.atlibs.grpc.steps.GrpcSteps;
import hello.Hello;
import hello.HelloServiceGrpc;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

@Tag("qa-module")
public class GrpcTests {

    HelloServiceGrpc.HelloServiceBlockingStub stub
            = HelloServiceGrpc.newBlockingStub(GrpcManager.getGrpcChannel("your-grpc-app"));

    @ParameterizedTest
    @ValueSource(strings = {"Ilya", "null"})
    void demoTest_unaryRequest(String name) throws InvalidProtocolBufferException {
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
    @ValueSource(strings = {"Ilya", "null"})
    void demoTest_ServiceStreamingRequest(String name) throws InvalidProtocolBufferException {
        GrpcSteps.prepareRequest(Hello.HelloRequest.class)
                .jsonBodyForRequestIs("unary-request-body.json")
                .metadataForRequestAre(Map.of("testKey", "testValue"))
                .<Hello.HelloRequest, Hello.HelloResponse>sendGrpcServiceStreamingRequest(req -> stub.lotsOfReplies(req))
                .jsonResponseIs("unary-response-body.json")
                .responseMetadataIs(Map.of(
                        "content-type", "application/grpc"
                ));
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"Ilya", "null"})
//    void demoTest_ClientStreaming(String name) throws InvalidProtocolBufferException {
//        GrpcSteps.prepareRequest(Hello.HelloRequest.class)
//                .jsonBodyForRequestIs("unary-request-body.json")
//                .metadataForRequestAre(Map.of("testKey", "testValue"))
//                .sendGrpcClientStreamingRequest(stub::lotsOfGreetings)
//                .jsonResponseIs("unary-response-body.json")
//                .responseMetadataIs(Map.of(
//                        "content-type", "application/grpc"
//                ));
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"Ilya", "null"})
//    void demoTest_BidirectionalStreaming(String name) throws InvalidProtocolBufferException {
//        GrpcSteps.prepareRequest(Hello.HelloRequest.class)
//                .jsonBodyForRequestIs("unary-request-body.json")
//                .metadataForRequestAre(Map.of("testKey", "testValue"))
//                .sendGrpcBidirectionalStreamingRequest(stub::BidiHello)
//                .jsonResponseIs("unary-response-body.json")
//                .responseMetadataIs(Map.of(
//                        "content-type", "application/grpc"
//                ));
//    }

}
