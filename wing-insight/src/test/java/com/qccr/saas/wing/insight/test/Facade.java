package com.qccr.saas.wing.insight.test;

import com.qccr.knife.result.Result;
import com.qccr.saas.wing.insight.Mock;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Facade {

    @Mock
    public Result<List<Person>> list(){
        return null;
    }
}
