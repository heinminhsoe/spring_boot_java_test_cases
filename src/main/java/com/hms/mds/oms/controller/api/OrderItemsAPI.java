package com.hms.mds.oms.controller.api;


import com.hms.mds.oms.dto.OrderItemsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "OrderItems API")
@RequestMapping("api/order/{orderId}/orderItems")
public interface OrderItemsAPI {

    @ApiOperation(value = "Get orderItemss", nickname = "getOrderItemss", response = OrderItemsDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping
    public ResponseEntity<List<OrderItemsDto>> findAll(@PathVariable("orderId") Integer orderId);

    @ApiOperation(value = "Get an orderItems by ID.", nickname = "getOrderItemsById", response = OrderItemsDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemsDto> findById(@PathVariable("orderId") Integer orderId, @PathVariable("id") Integer id);

    @ApiOperation(value = "Create orderItems", nickname = "createOrderItems", response = OrderItemsDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping
    public ResponseEntity<OrderItemsDto> create(
            @PathVariable("orderId") Integer orderId,
            @Valid @RequestBody OrderItemsDto orderItems);

    @ApiOperation(value = "Update orderItems", nickname = "updateOrderItems", response = OrderItemsDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemsDto> update(
            @PathVariable("orderId") Integer orderId,
            @PathVariable("id") Integer id,
            @Valid @RequestBody OrderItemsDto orderItems);

    @ApiOperation(value = "Delete orderItems", nickname = "deleteOrderItems")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("orderId") Integer orderId,
            @PathVariable("id") Integer id);
}
