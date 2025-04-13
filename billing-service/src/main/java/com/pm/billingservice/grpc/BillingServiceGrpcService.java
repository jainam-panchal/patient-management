package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingServiceGrpcService extends BillingServiceImplBase {


    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
        log.info("createBillingAccount request received {}", request.toString());

        // Business Logic - eg. save to database, perform calculation etc

        BillingResponse builder = BillingResponse.newBuilder()
                .setAccountId("791291")
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }
}
