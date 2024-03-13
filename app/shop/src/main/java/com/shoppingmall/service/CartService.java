package com.shoppingmall.service;

import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.dto.request.CartSaveRequestDto;
import com.shoppingmall.dto.response.CartResponseDto;
import com.shoppingmall.dto.response.CartTotalPriceResponseDto;
import com.shoppingmall.exception.FailDeleteCartProductException;
import com.shoppingmall.exception.FailSaveCartProductException;
import com.shoppingmall.exception.FailUpdateCartProductException;
import com.shoppingmall.mapper.CartMapper;
import com.shoppingmall.mapper.MemberMapper;
import com.shoppingmall.mapper.ProductMapper;
import com.shoppingmall.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {

    private final MemberMapper memberMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @Transactional
    public int addCartProduct(CartSaveRequestDto cartRequestDto) {
        int responseCode = 0;

        // 상품이 먼저 존재하는 지 한번 더 검증
        Optional<Product> product = productMapper.getProductByProductId(cartRequestDto.getProductId());
        if (product.isEmpty()) {
            throw new IllegalArgumentException("상품 정보를 찾을 수 없습니다. 다시 시도해주세요.");
        }

        // 해당 회원 장바구니에 등록된 상품이 있는지 확인
        int count = cartMapper.countCartProducts(cartRequestDto.toEntity());
        if (count > 0) {
            responseCode = cartMapper.updateCartProduct(cartRequestDto.toEntity()) > 0 ? 1 : 0; // 수정 성공 - 1
        } else {
            responseCode = cartMapper.addCartProduct(cartRequestDto.toEntity()) > 0 ? 2 : 0; // 등록 성공 - 2
        }

        // 장바구니 등록, 수정 실패 시 예외 발생!
        if (responseCode == 0) {
            throw new FailSaveCartProductException(ErrorCode.SAVE_CART);
        }
        return responseCode;
    }

    @Transactional
    public int updateCartProduct(CartSaveRequestDto cartRequestDto) {
        int responseCode = cartMapper.updateCartProduct(cartRequestDto.toEntity());
        if (responseCode == 0) {
            throw new FailUpdateCartProductException(ErrorCode.UPDATE_CART);
        }
        return responseCode;
    }

    public CartTotalPriceResponseDto getCartItems(Long memberId) {
        if (memberId == null) {
            return new CartTotalPriceResponseDto();
        }

        List<CartResponseDto> cartResponseDtos = cartMapper.getCartItems(memberId)
                .stream()
                .map(CartResponseDto::toDto)
                .collect(Collectors.toList());

        int totalPriceCartItem = getTotalPriceCartItems(memberId);

        return CartTotalPriceResponseDto.builder()
                .cartItems(cartResponseDtos)
                .totalPriceCartItem(totalPriceCartItem)
                .build();
    }

    /**
     * 장바구니의 모든 상품에 대한 총 합계 가격 반환
     * @param memberId
     * @return
     */
    private int getTotalPriceCartItems(Long memberId) {
        return cartMapper.getCartItemsTotalPrice(memberId);
    }

    @Transactional
    public int deleteCartItem(Long cartId, Long memberId) {
        int responseCode = cartMapper.deleteCartItem(cartId, memberId);
        if (responseCode == 0) {
            throw new FailDeleteCartProductException(ErrorCode.DELETE_CART);
        }
        return responseCode;
    }
}