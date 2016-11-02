package cn.wiesel.junit;

import java.util.List;

import cn.wiesel.tool.RedisManager;
import redis.clients.jedis.Jedis;

public class Test {

	@org.junit.Test
	public void redis() {
		RedisManager redisManager = RedisManager.getInstance();

		// redisManager.set("username", "黎明！");
		// redisManager.set("username", "不知道");

		Jedis jedis = new Jedis();
		// jedis.del("user");
		// // jedis.append("username", "wujian");
		// // jedis.append("username", "lilin");
		// // String username = redisManager.get("username");
		// jedis.rpush("user", "wujian");
		// jedis.rpush("user", "lilin");
		// System.out.println(jedis.lrange("user", 0, -1));
		redisManager.rpush("user", "wutao");
		redisManager.del("user");
		System.out.println(redisManager.llen("user"));

		// System.out.println(username);
	}

}