package cn.wiesel.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;

import cn.wiesel.domain.Region;
import cn.wiesel.tool.RedisManager;
import cn.wiesel.tool.TxQueryRunner;

/**
 * 
 * @ClassName: RegionDao
 * @Description:
 * @author: 吴建
 * @date: 2016年7月19日 上午11:37:07
 *
 */
public class RegionDao {
	private QueryRunner queryRunner = new TxQueryRunner();

	private RedisManager redisManager = RedisManager.getInstance();
	Jedis jedis = new Jedis();

	Logger logger = Logger.getLogger(getClass());

	// 该类的实例主要用于序列化和反序列化的操作
	Gson gson = new Gson();

	/**
	 * 查询省份信息
	 * 
	 * @return
	 */
	public List<Region> findByProvince() {

		List<Region> regionList = new ArrayList<Region>();
		jedis.del("province");
		// 如果缓存中没有相关数据则从数据库中读取,并将其存入缓存中
		if (!redisManager.exists("province")) {
			try {
				String sql = "select *from cap_region where TYPE=1";
				regionList = queryRunner.query(sql,
						new BeanListHandler<Region>(Region.class));

				// 进行序列化操作，将数据存储到缓存中
				for (int i = 0; i < regionList.size(); i++) {
					String s = gson.toJson(regionList.get(i));
					redisManager.rpush("province", s);
				}
				logger.info(jedis.lrange("province", 0, -1));
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {// 从缓存中读取数据,并将其反序列化，存储到list集合中
			for (int i = 0; i < redisManager.llen("province"); i++) {
				Region region = gson.fromJson(
						redisManager.lindex("province", i), Region.class);
				regionList.add(region);
			}
		}
		return regionList;
	}

	/**
	 * 通过相应省份名查询其下面相应的城市
	 * 
	 * @param province
	 * @return
	 */
	public List<Region> fingCityByProvince(String province) {

		List<Region> cityList = new ArrayList<Region>();
		jedis.del(province);
		// 判断缓存中是否存在相关省份下的城市信息，如果没有的话就从数据库中读取，并保存到缓存中
		if (!redisManager.exists(province)) {
			try {
				String sql = "select *from cap_region where NAME=?";
				List<Region> region = queryRunner.query(sql,
						new BeanListHandler<Region>(Region.class), province);

				String ID = region.get(0).getID();
				String sqlsString = "select *from cap_region where PARENT_ID=? and TYPE=2";
				cityList = queryRunner.query(sqlsString,
						new BeanListHandler<Region>(Region.class), ID);
		
				// 进行序列化操作，将数据存储到缓存中
				for (int i = 0; i < cityList.size(); i++) {
					String s = gson.toJson(cityList.get(i));
					redisManager.rpush(province, s);
				}
				logger.info(jedis.lrange(province, 0, -1));
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {// 从缓存中读取数据,并将其反序列化，存储到list集合中
			for (int i = 0; i < redisManager.llen(province); i++) {
				Region region = gson.fromJson(redisManager.lindex(province, i),
						Region.class);
				cityList.add(region);
			}
		}

		return cityList;
	}
}
