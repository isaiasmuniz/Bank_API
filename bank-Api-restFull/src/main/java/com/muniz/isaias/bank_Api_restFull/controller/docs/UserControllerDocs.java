package com.muniz.isaias.bank_Api_restFull.controller.docs;

import com.muniz.isaias.bank_Api_restFull.controller.UserController;
import com.muniz.isaias.bank_Api_restFull.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerDocs {

    @Operation(summary = "Find by id", description = "Finds a user by id", tags = "users", responses = {
            @ApiResponse(description = "Success", responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    UserDTO findUserById(@Parameter(description = "user id", example = "1", required = true)
                         @PathVariable("id") Long id);

    @Operation(summary = "Update a user", description = "Updates an user", tags = "users", responses = {
            @ApiResponse(description = "Success", responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    UserDTO update(@RequestBody UserDTO user);

    @Operation(summary = "Create a user", description = "Creates an user", tags = "users", responses = {
            @ApiResponse(description = "Success", responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
    })
    UserDTO create(@RequestBody UserDTO user);
}
