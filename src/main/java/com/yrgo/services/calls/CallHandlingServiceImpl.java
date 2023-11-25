package com.yrgo.services.calls;

import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class CallHandlingServiceImpl implements CallHandlingService {

    @Autowired
    private CustomerManagementService customerService;
    @Autowired
    private DiaryManagementService diaryService;

    @Override
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException, RecordNotFoundException{
        customerService.recordCall(customerId, newCall);
        actions.forEach(action -> diaryService.recordAction(action));


        // Eller, med metodreferens:
        // actions.forEach(diaryService::recordAction);
    }
}
