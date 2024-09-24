package com.solon.airbnb.email.application.service;

import com.solon.airbnb.shared.service.GenericServiceBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED)
public class EMailDelegateServiceBean extends GenericServiceBean implements EMailDelegateService{
}
