//package com.hsx.sa.service.impl.out.admin;
//
//import com.hsx.sa.generators.AdminRejectGenerator;
//import com.hsx.sa.metrics.logger.MessageMetricsLogger;
//import com.hsx.sa.service.AbstractService;
//import com.hsx.sa.service.impl.out.CommonUtilService;
//import com.hsx.sa.transform.xb.XBAdminRejectTransformer;
//import com.hsx.sa.util.AdminRejectUtils;
//import com.hsx.sa.util.XBMarshalUtil;
//import com.hsx.common.model.RequestsHolder;
//import com.hsx.common.model.constants.Constants;
//import com.hsx.common.model.constants.MessageType;
//import com.bcs.xborder.common.util.model.XBGWMessage;
//import com.bcs.xborder.gw.HdrAndData;
//import com.bcs.xborder.model.common.enums.EndPointInterface;
//import com.bcs.xborder.model.xb.XBAdminReject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.Message;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.async.DeferredResult;
//
//@Component("maAdminRejectService")
//public class MAAdminRejectServiceImpl extends AbstractService<Void, XBGWMessage> {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(MAAdminRejectServiceImpl.class);
//
//    @Autowired
//    private XBMarshalUtil xbMarshalUtil;
//
//    @Autowired
//    private XBAdminRejectTransformer xbAdminRejectTransformer;
//    @Autowired
//    private MessageMetricsLogger messageMetricsLogger;
//    @Autowired
//    private CommonUtilService commonUtilService;
//
//    @Autowired
//    private AdminRejectUtils adminRejectUtils;
//
//    @Autowired
//    private AdminRejectGenerator adminRejectGenerator;
//
//    @Autowired
//    private RequestsHolder requestsHolder;
//
//
//    @Override
//    public Message<Void> executeService(Message<XBGWMessage> msg) throws ServiceException, InterruptedException {
//
//        MessageType MessageType = (com.hsx.common.model.constants.MessageType) msg.getHeaders().get(Constants.MessageHeaders.MESSAGE_TYPE.name());
//        EndPointInterface endpointInterface = msg.getHeaders().get(Constants.MessageHeaders.MESSAGE_TYPE.name(), EndPointInterface.class);
//        String adminRejectReason = msg.getHeaders().get(Constants.MessageHeaders.ADMIN_REJECT_REASON.name(), String.class);
//        String additionalData = msg.getHeaders().get(Constants.MessageHeaders.ADMIN_REJECT_ADDITIONAL_DATA.name(), String.class);
//        String reasonDescription = msg.getHeaders().get(Constants.MessageHeaders.ADMIN_REJECT_REASON_DESCRIPTION.name(), String.class);
//        String errorLocation = msg.getHeaders().get(Constants.MessageHeaders.ADMIN_REJECT_ERROR_LOCATION.name(), String.class);
//        String instructionId = msg.getHeaders().get(Constants.MessageHeaders.INSTRUCTION_ID.name(), String.class);
//
//        DeferredResult<ResponseEntity<String>> deferredResult = null;
//
//        deferredResult = requestsHolder
//                .getDeferredResult(instructionId);
//
//        if (deferredResult != null) {
//            LOGGER.info("Admin Reject For REST Request | Instruction ID {} ", instructionId);
//            try {
//                XBAdminReject xbAdminReject = adminRejectGenerator.generateXBAdminReject(msg, (String) msg.getHeaders().get(Constants.MessageHeaders.INSTRUCTION_ID.name()), adminRejectReason, additionalData, reasonDescription, errorLocation);
//                com.bcs.xborder.gw.HdrAndData gwXmlObject = xbAdminRejectTransformer.convertModelObjToXMLObj(xbAdminReject, new HdrAndData());
//
//                if (deferredResult != null) {
//                    deferredResult.setResult(ResponseEntity.ok(xbMarshalUtil.marshal(gwXmlObject)));
//                }
//            } catch (Exception e) {
//                LOGGER.info("Error Occurred In Admin Reject | Instruction ID {} ", instructionId);
//                throw new ServiceException(e.getMessage());
//            }
//        } else {
//            LOGGER.info("Admin Reject For Other Flows | Instruction ID {} ", instructionId);
//            adminRejectUtils.adminReject(msg, endpointInterface, (String) msg.getHeaders().get(Constants.MessageHeaders.INSTRUCTION_ID.name()), adminRejectReason, additionalData, reasonDescription, errorLocation);
//        }
//
//        return null;
//    }
//
//
//}
//
