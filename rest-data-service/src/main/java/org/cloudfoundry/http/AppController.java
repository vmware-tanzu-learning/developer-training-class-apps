package org.cloudfoundry.http;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

	@Value("${vcap.application.name:<app-name>}")
	private String appName;

	@Value("${vcap.application.instance_index:<instance-index>}")
	private String appInstanceIndex;

	private DataSource db;

	@Value("${ROSTER_A:#{null}}")
	private String rosterVarA;

	@Value("${ROSTER_B:#{null}}")
	private String rosterVarB;

	@Value("${ROSTER_C:#{null}}")
	private String rosterVarC;

	@Value("${APP_VERSION:#{null}}")
	private String appVersion;

	@Autowired
	public AppController(DataSource db) {
		this.db = db;
	}

	@RequestMapping("/kill")
	public void kill() {
		System.exit(1);
	}

	@RequestMapping("/app-details")
	public AppDetails info() throws SQLException {
		Map<String, String> map = new HashMap<>();
		if (rosterVarA != null) {
			map.put("ROSTER_A", rosterVarA);
		}
		if (rosterVarB != null) {
			map.put("ROSTER_B", rosterVarB);
		}
		if (rosterVarC != null) {
			map.put("ROSTER_C", rosterVarC);
		}
		if (map.isEmpty()) {
			map = null;
		}
		return new AppDetails(appName, appInstanceIndex,
				db.getConnection().getMetaData().getURL(), this.appVersion, map);

	}
}
