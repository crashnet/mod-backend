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
import mediaset.Group;
import mediaset.Program;
import mediaset.Section;

@Component
public class CacheMediasetProgramSectionsThread extends Thread {

	@Autowired
	private SessionManagement sessionManagement;

	final static Logger logger = Logger.getLogger(CacheMediasetProgramSectionsThread.class);

	private List<Future<Program>> lista_sezioni_future = new ArrayList<Future<Program>>();

	public CacheMediasetProgramSectionsThread(SessionManagement sessionManagement) {
		this.sessionManagement = sessionManagement;
	}

	public void run() {
		logger.info("CacheSectionsThread start");

		ExecutorService executor = Executors.newFixedThreadPool(10);

		logger.info("CacheSectionsThread running ...");
		Set<String> keys = sessionManagement.getProgrammi().keySet();
		Program program = null;
		int i = 0;
		for (String key : keys) {
			program = sessionManagement.getProgrammi().get(key);
			i++;
			Callable<Program> callable = new MyCallableSections(program);

			Future<Program> future = executor.submit(callable);

			lista_sezioni_future.add(future);

//			if (i >= 1)
//				break;
		}

		for (Future<Program> fut : lista_sezioni_future) {
			List<Group> list_groups = sessionManagement.getArchivio().getProgrammi().getGroup();

			for (Group g : list_groups) {
				List<Program> pr_list = g.getProgram();
				for (Program p : pr_list) {
				}
			}

			logger.info("CacheSectionsThread end futures");

		}
		logger.info("CacheSectionsThread stopping ...");
		executor.shutdown();
	}
}