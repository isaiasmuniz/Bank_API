package com.muniz.isaias.bank_Api_restFull.controller.docs;

import com.muniz.isaias.bank_Api_restFull.controller.TransactionController;
import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TransactionControllerDocs {

    @Operation(summary = "Find all transactions", description = "Returns the transaction history for the specified account.", tags = "transaction", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class)))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<TransactionDTO>>> viewHistory(@Parameter(description = "Account id", example = "1", required = true)
                                                                        @PathVariable("id") Long id,
                                                                        @Parameter(description = "page", example = "0", required = true)
                                                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                        @Parameter(description = "size", example = "2", required = true)
                                                                        @RequestParam(value = "size", defaultValue = "2") Integer size,
                                                                        @Parameter(description = "direction", example = "asc", required = true)
                                                                        @RequestParam(value = "direction", defaultValue = "asc") String direction);


    @Operation(summary = "Deposit", description = "Deposits money into an account.", tags = "transaction", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    TransactionDTO deposit(@RequestBody TransactionDTO transactionDTO,
                           @Parameter(description = "ID of the account that will receive the deposit", example = "1", required = true)
                           @PathVariable("id") Long id);

    @Operation(summary = "withdrawal", description = "Withdraws money from an account.", tags = "transaction", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    TransactionDTO withdrawal(@RequestBody TransactionDTO transactionDTO,
                              @Parameter(description = "ID of the account that will be withdrawn", example = "1", required = true)
                              @PathVariable("id") Long id);

    @Operation(summary = "Transfer", description = "Transfers money between two accounts.", tags = "transaction", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    TransactionDTO bankTransfer(@RequestBody TransactionDTO transactionDTO,
                                @Parameter(description = "Id of the origin account", example = "1", required = true)
                                @PathVariable("id") Long id,
                                @Parameter(description = "ID of the targetAccount", example = "2", required = true)
                                @PathVariable("target_id") Long targetId);


}

