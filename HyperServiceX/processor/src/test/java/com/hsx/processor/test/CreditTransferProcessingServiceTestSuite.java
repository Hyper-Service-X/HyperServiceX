package com.hsx.processor.test;

import com.hsx.processor.webconfig.ApplicationConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ContextConfiguration;


@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {ApplicationConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
//Uncomment it if you want Spring context to load
//@SpringBootTest
public class CreditTransferProcessingServiceTestSuite {
//    @Autowired
//    @InjectMocks
//    private CreditTransferProcessingServiceImpl creditTransferProcessingService;
//
//    @Mock
//    private DMCreditTransferDAO creditTransferDAO;

    @Test
    @DisplayName("Test DM Credit Transfer Processing Service - persistRequest Success")
    public void testDMCreditTransferProcessingPersistRequestSuccess() throws Exception {
//        DMCreditTransfer mocked = new DMCreditTransfer.Builder().addCreditTransferTxn(new DMCreditTransferTxn.Builder().setInstdAgtMmbId("BCSSSGTHXXX").build()).build();
//        Mockito.doNothing().when(creditTransferDAO).save(any(), any());
//        DMCreditTransferCacheModel dmCreditTransferCacheModel = creditTransferProcessingService.persistRequest(mocked, "20200512OCBCSGSGBRT2191228");
//
//        assertEquals("BCSSSGTH",dmCreditTransferCacheModel.getBcsGwBic(),  "BCS GW BIC Code Should be Instructed Agent Member Id");
//        assertEquals("20200512OCBCSGSGBRT2191228", dmCreditTransferCacheModel.getInstrId(), "DM Instruction should be matched");
//        assertEquals(XBConstants.CTStatus.REQUEST_WAITING_CANCELLATION.getName(), dmCreditTransferCacheModel.getProcessingStatus(), "Processing Status should be " + XBConstants.CTStatus.REQUEST_WAITING_CANCELLATION.getName());
//        assertEquals(XBConstants.Status.PENDING.name(), dmCreditTransferCacheModel.getStatus(), "DM Instruction should be " + XBConstants.Status.PENDING.name());
//        assertEquals(XBConstants.Direction.OUTBOUND.name(),dmCreditTransferCacheModel.getDirection(),  "Direction should be " + XBConstants.Direction.OUTBOUND.name() + " for DM CTs");
//        assertTrue(dmCreditTransferCacheModel.getLastUpdatedTime() > 0, "Last updated time should be set");
//        assertTrue(dmCreditTransferCacheModel.getRequestRetryCount() == 0, "Retry count should be init to 0");
//        assertTrue(dmCreditTransferCacheModel.getRequestReceivedTime() > 0, "Request received time should be set");
    }

    @Test
    @DisplayName("Test DM Credit Transfer Processing Service - persistRequest Exception")
    public void testDMCreditTransferProcessingPersistRequestException() throws Exception {
//        DMCreditTransfer mocked = new DMCreditTransfer.Builder().addCreditTransferTxn(new DMCreditTransferTxn.Builder().setInstdAgtMmbId("BCSSSGTHXXX").build()).build();
//
//        Mockito.doThrow(new XBDAOException("")).when(creditTransferDAO).save(any(), any());
//        Exception exception = assertThrows(XBDAOException.class, () -> {
//            creditTransferProcessingService.persistRequest(mocked, "20200512OCBCSGSGBRT2191228");
//        });
//        assertTrue(exception instanceof XBDAOException, "XBDAOException Exception should be thrown");
//        assertNotNull(exception.getMessage(), "XBDAOException should have detailed error");
    }

    @Test
    @DisplayName("Test DM Credit Transfer Processing Service - process Exception")
    public void testDMCreditTransferProcessingProcessException() throws Exception {
//        DMCreditTransfer mocked = new DMCreditTransfer.Builder().addCreditTransferTxn(new DMCreditTransferTxn.Builder().setInstdAgtMmbId("BCSSSGTHXXX").build()).build();
//
//        Mockito.doThrow(new XBDAOException("")).when(creditTransferDAO).save(any(), any());
//        Exception exception = assertThrows(XBDAOException.class, () -> {
//            creditTransferProcessingService.persistRequest(mocked, "20200512OCBCSGSGBRT219122");
//        });
//        assertTrue(exception instanceof XBDAOException, "XBDAOException Exception should be thrown");
//        assertNotNull(exception.getMessage(), "XBDAOException should have detailed error");
    }
}
