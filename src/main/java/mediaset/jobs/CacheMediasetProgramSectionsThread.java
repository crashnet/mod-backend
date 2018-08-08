package mediaset.jobs;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import mediaset.Program;
import mediaset.Section;

public class CacheMediasetProgramSectionsThread extends Thread {

	private Map<String, Program> programmi;
	private List<Future<Map<String, Section>>> lista_sezioni_future = new ArrayList<Future<Map<String, Section>>>();
	
	public CacheMediasetProgramSectionsThread(Map<String, Program> programmi) {
		this.programmi = programmi;
	}

	public void run() {
		System.out.println("CacheSectionsThread running");

		ThreadPoolExecutor tpe = new ThreadPoolExecutor(10, 20 ,10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		ExecutorService executor = Executors.newCachedThreadPool();

		Set<String> keys = programmi.keySet();
		
		for (String key : keys) {
			Program program = programmi.get(key);
			
			Callable<Map<String, Section>> callable = new MyCallableSections(program);
		//	System.out.println("Program TEST: "+ program);
			Future<Map<String, Section>> future = executor.submit(callable);
			
			lista_sezioni_future.add(future);
		}

		for (Future<Map<String, Section>> fut : lista_sezioni_future) {
//			try {
//	//			System.out.println(new Date() + "::" +this.getName()+"::"+ fut.get());
//			} catch (InterruptedException | ExecutionException e) {
//				e.printStackTrace();
//			}
		}
		System.out.println("end job");
		executor.shutdown();
	}
}