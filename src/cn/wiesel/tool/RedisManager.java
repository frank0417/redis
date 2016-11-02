package cn.wiesel.tool;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.bosssoft.platform.ticketcenter.server.cfg.Configuration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 管理器
 * 
 * @author wengzr
 *
 */
public class RedisManager {
	
	private static final String SINGLE_NODE_REGEX = "([^:]+):([\\d]+)";
	
	private static final Pattern SINGLE_NODE_PATTERN = Pattern.compile(SINGLE_NODE_REGEX);
	
    private String host ="127.0.0.1:6379";
    
    private int timeout = 0; // Timeout for Jedis try to connect to redis server
    
    private String password = "";

    private JedisPool jedisPool   = null;
    
    private static RedisManager INSTANCE=null;
    
    
    private RedisManager(){
        init();
    }

    //获取RedisMannger的实例
    public synchronized static RedisManager getInstance(){
    	if(INSTANCE==null){
    		INSTANCE=new RedisManager();
    	}
    	return INSTANCE;
    }
    /**
     * Initializing jedis pool to connect to Jedis.
     */
    public void init() {

    	if(jedisPool==null){
//    		host=Configuration.getRedisAddress();
//    		password=Configuration.getRedisPassword();
//    		timeout=Configuration.getRedisTimeout();
    		host ="127.0.0.1:6379";
    		password="";
            timeout=0;
            
			if(host==null)
				throw new IllegalArgumentException("Not found redis host!");
			
			final Matcher singleNodeMatcher = SINGLE_NODE_PATTERN.matcher(host);
			
			if (singleNodeMatcher.matches()) { // for single
				final String hostname = singleNodeMatcher.group(1);
				final int port = Integer.parseInt(singleNodeMatcher.group(2));
	
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxIdle(5);
				config.setMaxTotal(300);
				config.setTestOnBorrow(false);
				
				if(password != null && !"".equals(password)){
					jedisPool = new JedisPool(config, hostname, port, timeout, password);
				}else if(timeout != 0){
					jedisPool = new JedisPool(config, hostname, port,timeout);
				}else{
					jedisPool = new JedisPool(config, hostname, port);
				}
				
			}
    	}
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * Get value from Redis
     * @param key
     * @return
     */
    public String get(String key){
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.get(key);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }

    /**
     * Set value into Redis with default time to live in seconds.
     * @param key
     * @param value
     */
    public void set(String key, String value){
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.set(key, value);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }
    
    public void expire(String key,int timeToLiveSeconds){
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.expire(key, timeToLiveSeconds);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }

    /**
     * Set value into Redis with specified time to live in seconds.
     * @param key
     * @param value
     * @param timeToLiveSeconds
     */
    public void setex(String key, String value, int timeToLiveSeconds){
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.setex(key, timeToLiveSeconds, value);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }

    /**
     * Delete key and its value from Jedis.
     * @param key
     */
    public void del(String key){
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.del(key);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }
    
    
    /**
     * check key exist from Jedis.
     * @param key
     */
    public boolean exists(String key){
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }
    
    

    /**
     * Get keys matches the given pattern.
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.keys(pattern);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }

    /**
     * Get multiple values for the given keys.
     * @param keys
     * @return
     */
    public Collection<String> mget(String... keys) {
        if (keys == null || keys.length == 0) {
           return Collections.emptySet();
        }

        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.mget(keys);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }

    /**
     * Hash操作:返回名称为key的hash中所有的键（field）及其对应的value
     * @param key
     * @return
     */
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hgetAll(key);
		} finally {
			if (jedis != null)
				jedis.close();
		}

	}
	
    /**
     * Hash操作:返回名称为key的hash中field对应的value
     * @param key
     * @return
     */
	public String hget(String key,String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hget(key, field);
		} finally {
			if (jedis != null)
				jedis.close();
		}

	}

	/**
	 * 集合Set操作:向名称为key的set中添加元素member
	 * @param key
	 * @param members
	 * @return
	 */
	public Long sadd(String key, String... members) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.sadd(key, members);
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}
	
	/**
	 * 集合Set操作:返回名称为key的set的所有元素
	 * @param key
	 * @return
	 */
	public java.util.Set<String> smembers(String key) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.smembers(key);
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	/**
	 * Hash操作:向名称为key的hash中添加元素field 
	 * @param key
	 * @param hash
	 * @return
	 */
	public String hmset(String key, java.util.Map<String, String> hash) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hmset(key, hash);
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

    /**
     * Publish message to channel using subscribe and publish protocol.
     * @param channel
     * @param value
     */
    public void publish(String channel, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.publish(channel, value);
        } finally {
        	if (jedis != null)
        		jedis.close();
        }
    }
    
	public Long srem(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.srem(key, members);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 向名称为key的hash中添加元素field
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hset(key, field, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 删除名称为key的hash中键为field的域
	 * @param hkey
	 * @param field
	 * @return
	 */
	public Long hdel(String hkey, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hdel(hkey, field);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}


	/**
	 * 返回名称为key的hash中field i对应的value
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<String> hmget(String key, String... fields) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hmget(key, fields);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 返回名称为key的hash中所有键
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hkeys(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 在名称为key的list头添加一个值为value的 元素
	 * @param key
	 * @param value
	 */
	public Long lpush(String key,String value){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lpush(key, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	/**
	 * 返回名称为key的list中index位置的元素
	 * @param key
	 * @param index
	 */
	public String lindex(String key,int index){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lindex(key, index);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 在名称为key的list尾添加一个值为value的元素
	 * @param key
	 * @param value
	 * @return
	 */
	public Long rpush(String key,String value){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.rpush(key, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	/**
	 * 返回名称为key的list的长度
	 * @param key
	 * @return
	 */
	public Long llen(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.llen(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 返回并删除名称为key的list中的首元素
	 * @param key
	 * @return
	 */
	public String lpop(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lpop(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 返回并删除名称为key的list中的尾元素
	 * @param key
	 * @return
	 */
	public String rpop(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.rpop(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
    public String getHost() {
        return host;
    }

    /**
     * Host Address形式如:127.0.0.1:6379
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }
    
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}