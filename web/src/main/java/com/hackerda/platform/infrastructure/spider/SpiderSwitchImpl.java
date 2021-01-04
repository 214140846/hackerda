package com.hackerda.platform.infrastructure.spider;

import com.hackerda.platform.domain.SpiderSwitch;
import org.springframework.stereotype.Service;

@Service
public class SpiderSwitchImpl implements SpiderSwitch {
    @Override
    public boolean fetchUrp() {
        return false;
    }
}
