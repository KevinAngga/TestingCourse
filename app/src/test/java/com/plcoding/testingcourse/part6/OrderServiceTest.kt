package com.plcoding.testingcourse.part6

import androidx.compose.runtime.currentComposer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderServiceTest {

    lateinit var auth: FirebaseAuth
    lateinit var emailClient: EmailClient
    lateinit var orderService: OrderService

    @BeforeEach
    fun setUp() {
        //can be this
        /*auth = mockk(relaxed = true)
        emailClient = mockk(relaxed = true)
        orderService = OrderService(
            auth = auth,
            emailClient = emailClient
        )*/

        //more convinient test
        val firebaseUser = mockk<FirebaseUser>(relaxed = true) {
            every { isAnonymous } returns false
        }
        auth = mockk(relaxed = true) {
            every { currentUser } returns firebaseUser
        }

        emailClient = mockk(relaxed = true)
        orderService = OrderService(
            auth = auth,
            emailClient = emailClient
        )
    }

    @Test
    fun `place order with correct email`() {
        val customer = Customer(
            id = 1,
            email = "test@gmail.com"
        )
        orderService.placeOrder(
            customerEmail = customer.email,
            productName = "Book"
        )

        verify {
            emailClient.send(
                Email(
                    subject = "Order Confirmation",
                    content ="Thank you for your order of Book.",
                    recipient = customer.email
                )
            )
        }
    }

}