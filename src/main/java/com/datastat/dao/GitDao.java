package com.datastat.dao;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

@Repository
public class GitDao {
    @Autowired
    Environment env;

    static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private static final Logger logger =  LoggerFactory.getLogger(GitDao.class);

    GitDao() {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                GitPull(env.getProperty("repo.path"));
            }
        }, 5, 3600, TimeUnit.SECONDS);
    }

    public void GitPull(String repoPath) {
        try {
            FileRepository localRepo = new FileRepository(repoPath + "/.git");
            Git git = new Git(localRepo);
            git.pull().call();
            git.close();
        } catch (Exception e) {
            logger.error("git pull exception", e);
        }
    }
}
