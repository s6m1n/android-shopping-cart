package woowacourse.shopping.presentation.ui.detail

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.shopping.InstantTaskExecutorExtension
import woowacourse.shopping.cart
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.presentation.ui.UiState
import woowacourse.shopping.product
import woowacourse.shopping.shoppingProduct

@ExtendWith(InstantTaskExecutorExtension::class)
@ExtendWith(MockKExtension::class)
class ProductDetailViewModelTest {
    @MockK
    private lateinit var productRepository: ProductRepository

    @MockK
    private lateinit var cartRepository: CartRepository

    @InjectMockKs
    private lateinit var viewModel: ProductDetailViewModel

    @Test
    fun `loadById로 특정 상품의 데이터를 가져온다`() {
        every { productRepository.loadById(any()) } returns Result.success(product)
        every { cartRepository.find(any()) } returns Result.success(cart)
        viewModel.loadProductById(0)
        Assertions.assertEquals(
            viewModel.shoppingProduct.getOrAwaitValue(1),
            UiState.Success(shoppingProduct),
        )
    }

    @Test
    fun `loadById로 특정 상품의 데이터를 가져오기 실패하면 해당하는 Error 상태로 전환한다`() {
        every { productRepository.loadById(any()) } returns Result.failure(Throwable())
        viewModel.loadProductById(0)
        Assertions.assertEquals(
            viewModel.error.getOrAwaitValue(1).getContentIfNotHandled(),
            DetailError.ProductItemsNotFound,
        )
    }

    @Test
    fun `초기화 후 장바구니에 담기 버튼이 눌리면 상품과 수량을 저장한다`() {
        // given
        every { productRepository.loadById(any()) } returns Result.success(product)
        every { cartRepository.find(any()) } returns Result.success(cart)
        every {
            cartRepository.setQuantity(shoppingProduct.toProduct(), shoppingProduct.quantity)
        } returns Result.success(shoppingProduct.id)

        // when
        viewModel.loadProductById(0)
        viewModel.saveCartItem()

        // then
        Assertions.assertEquals(
            viewModel.addCartEvent.getOrAwaitValue(1).getContentIfNotHandled(),
            shoppingProduct.quantity,
        )
    }
}
