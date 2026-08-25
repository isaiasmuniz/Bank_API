package com.muniz.isaias.bank_Api_restFull.controller.docs;


import com.muniz.isaias.bank_Api_restFull.controller.AccountController;
import com.muniz.isaias.bank_Api_restFull.dto.AccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;

public interface AccountControllerDocs {

    @Operation(summary = "Find by id", description = "Finds an account by id", tags = "accounts", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    AccountDTO findAccountById(@Parameter(description = "account id", example = "1", required = true)
                               @PathVariable("id") Long id);

    @Operation(summary = "Create an account", description = "Creates an account", tags = "accounts", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    AccountDTO createAccount(@Parameter(description = "account id", example = "1", required = true)
                             @PathVariable("id") Long id);

    @Operation(summary = "Block account", description = "Blocks account", tags = "accounts", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    AccountDTO blockAccount(@Parameter(description = "account id", example = "1", required = true)
                            @PathVariable("id") Long id);

    @Operation(summary = "Unblock an account", description = "Unblocks an account", tags = "accounts", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    AccountDTO unBlockAccount(@Parameter(description = "account id", example = "1", required = true)
                              @PathVariable("id") Long id);
}
