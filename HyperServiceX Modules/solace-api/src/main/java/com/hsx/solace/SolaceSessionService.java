package com.hsx.solace;

import com.hsx.solace.jcsmp.SpringJCSMPFactory;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "hsx.messaging.service", havingValue = "solace", matchIfMissing = true)
public class SolaceSessionService {

    private final List<HSXJCSMPSession> hsxJCSMPSessions;
    private SpringJCSMPFactory solaceFactory;
    private final Logger LOGGER = LoggerFactory.getLogger(SolaceSessionService.class);


    public SolaceSessionService(ApplicationContext ctx) throws InvalidPropertiesException {
        this.solaceFactory = ctx.getBean(SpringJCSMPFactory.class);
        this.hsxJCSMPSessions = getHsxJCSMPSessions();
    }

    public List<HSXJCSMPSession> getAllSessions() {
        return hsxJCSMPSessions;
    }

    public List<HSXJCSMPSession> getAllExceptDefaultSessions() {
        List<HSXJCSMPSession> sessions = new ArrayList<>();
        for (int i = 0; i < hsxJCSMPSessions.size(); i++) {
            if (i != solaceFactory.getDefaultSiteNo() - 1) {
                sessions.add(hsxJCSMPSessions.get(i));
            }
        }
        return sessions;
    }

    public HSXJCSMPSession getDefaultSession() {
        return hsxJCSMPSessions.get(solaceFactory.getDefaultSiteNo() - 1);
    }

    public HSXJCSMPSession getSession(int siteNo) {
        return this.hsxJCSMPSessions.get(siteNo-1);
    }

    public void connectToAllSessions() {
        for (HSXJCSMPSession HSXJCSMPSession : hsxJCSMPSessions) {
            try {
                HSXJCSMPSession.getSession().connect();
            } catch (Exception e) {
                LOGGER.error("Error in connecting to session : {}", e.getMessage());
            }
        }
    }

    public void closeAllSessions() {
        for (HSXJCSMPSession HSXJCSMPSession : hsxJCSMPSessions) {
            try {
                HSXJCSMPSession.getSession().closeSession();
            } catch (Exception e) {
                LOGGER.error("Error in closing session : {}", e.getMessage());
            }
        }
    }

    private List<HSXJCSMPSession> getHsxJCSMPSessions() throws InvalidPropertiesException {
        List<HSXJCSMPSession> sessions = new ArrayList<>();

        List<JCSMPSession> jcsmpSessions = solaceFactory.createSessions();
        for (int i = 0; i < jcsmpSessions.size(); i++) {
            sessions.add(new HSXJCSMPSession(jcsmpSessions.get(i), i + 1));
        }

        return sessions;
    }
}
