package cn.wiesel.service;

import java.util.List;

import cn.wiesel.dao.RegionDao;
import cn.wiesel.domain.Region;

/**
 * 
 * @ClassName: RegionService
 * @Description:
 * @author: 吴建
 * @date: 2016年7月19日 上午11:37:11
 *
 */
public class RegionService {
	private RegionDao regionDao = new RegionDao();

	/**
	 * 查询省份信息
	 * @return
	 */
	public List<Region> findByProvince() {
		return regionDao.findByProvince();
	}

	/**
	 * 通过相应省份名查询其下面相应的城市
	 * @param province
	 * @return
	 */
	public List<Region> findCityByProvince(String province) {
		return regionDao.fingCityByProvince(province);
	}
}
