package com.hlsii.controller;//package com.hlsii.controller;
//
//
//
///**
// * @author Shangcong Xin
// * @date 4/1/20
// */
//
//import com.hlsii.util.BeanValidators;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.validation.ConstraintViolationException;
//import javax.validation.Validator;
//import java.util.List;
//
//
//
//public abstract class BaseController {
//    protected Logger logger = LoggerFactory.getLogger(getClass());
//
//    /**
//     * Bean instance validate object
//     */
//    @Autowired
//    protected Validator validator;
//
//    /**
//     * Add Flash Message
//     *
//     * @param message
//     */
//    protected void addMessage(RedirectAttributes redirectAttributes,
//                              String... messages) {
//        StringBuilder sb = new StringBuilder();
//        for (String message : messages) {
//            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
//        }
//        redirectAttributes.addFlashAttribute("message", sb.toString());
//    }
//
//    /**
//     * Add message to Model
//     *
//     * @param message
//     */
//    protected void addMessage(Model model, String... messages) {
//        StringBuilder sb = new StringBuilder();
//        for (String message : messages) {
//            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
//        }
//        model.addAttribute("message", sb.toString());
//    }
//
//    /**
//     * Validate the parameters in server
//     *
//     * @param object
//     *            object to be validated
//     * @param groups
//     *            validate groups
//     * @return true when validate successful, otherwise put the error message into message.
//     */
//    protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
//        try {
//            BeanValidators.validateWithException(validator, object, groups);
//        } catch (ConstraintViolationException ex) {
//            List<String> list = BeanValidators.extractPropertyAndMessageAsList(
//                    ex, ": ");
//            list.add(0, "Data validating failed:");
//            addMessage(model, list.toArray(new String[] {}));
//            return false;
//        }
//        return true;
//    }
//}
