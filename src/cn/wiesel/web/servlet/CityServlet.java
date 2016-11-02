package cn.wiesel.web.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.wiesel.domain.Region;
import cn.wiesel.service.RegionService;
/**
 * 
 * @ClassName:  CityServlet
 * @Description: 
 * @author:  吴建
 * @date:    2016年7月19日 下午11:53:53
 *
 */
public class CityServlet extends HttpServlet {

	private RegionService regionService = new RegionService();
	Logger logger = Logger.getLogger(getClass());

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");

		String province = request.getParameter("province");
			
		List<Region> regionList = regionService.findCityByProvince(province);
		StringBuilder sb = listToStringBuilder(regionList);
		response.getWriter().print(sb);
	}

	private StringBuilder listToStringBuilder(List<Region> regionList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < regionList.size(); i++) {
			sb.append(regionList.get(i).getNAME());// 把每个属性的值存放到sb中。
			if (i < regionList.size() - 1) {
				sb.append(",");
			}
		}
		return sb;
	}
}
