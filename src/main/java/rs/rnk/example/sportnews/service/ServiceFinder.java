package rs.rnk.example.sportnews.service;


public interface ServiceFinder {
	
	Service find(String serviceName);
	
	public class DefaultServiceFinder implements ServiceFinder {

		@Override
		public Service find(String serviceName) {
			Service service = null;
			try {
				char firstLetter = Character.toUpperCase(serviceName.charAt(0));
				String className = firstLetter + serviceName.substring(1);
				Class<?> klass = Class.forName("rs.rnk.example.sportnews.service." + className);
				service = (Service) klass.getDeclaredConstructor().newInstance();
				return service;
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
	}
}
