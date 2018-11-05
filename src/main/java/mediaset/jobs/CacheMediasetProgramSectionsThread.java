package mediaset.jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import configuration.SessionManagement;
import mediaset.Program;
import mediaset.Section;

@Component
public class CacheMediasetProgramSectionsThread extends Thread {

	
	@Autowired
	private SessionManagement sessionManagement;
	
	final static Logger logger = Logger.getLogger(CacheMediasetProgramSectionsThread.class);


	private List<Future<Map<String, Section>>> lista_sezioni_future = new ArrayList<Future<Map<String, Section>>>();


	public CacheMediasetProgramSectionsThread(SessionManagement sessionManagement) {
		this.sessionManagement = sessionManagement;
	}

	public void run() {
		logger.info("CacheSectionsThread start");

		ExecutorService executor = Executors.newCachedThreadPool();

		logger.info("CacheSectionsThread running ...");
		Set<String> keys = sessionManagement.getProgrammi().keySet();
		Program program = null;
		int i = 0;
		for (String key : keys) {
			program = sessionManagement.getProgrammi().get(key);
			i++;
			Callable<Map<String, Section>> callable = new MyCallableSections(program);

			Future<Map<String, Section>> future = executor.submit(callable);

			lista_sezioni_future.add(future);

//			if (i >= 10) break;
		}

		for (Future<Map<String, Section>> fut : lista_sezioni_future) {
			try {
				logger.debug((new Date() + "::" + this.getName() + "::" + fut.get()));
//				Sections p = new Sections();
//				cache_sezioni.put(program.getId(), p);
					} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logger.info("CacheSectionsThread end futures");

		}
		logger.info("CacheSectionsThread stopping ...");
		executor.shutdown();
	}
}