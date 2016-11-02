package cn.wiesel.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.wiesel.domain.Region;
import cn.wiesel.service.RegionService;
import cn.wiesel.tool.BaseServlet;

/**
 * 
 * @ClassName: RegionServlet
 * @Description:
 * @author: 吴建
 * @date: 2016年7月19日 上午11:37:23
 *
 */
public class RegionServlet extends HttpServlet {

	private RegionService regionService = new RegionService();
	Logger logger = Logger.getLogger(getClass());

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");

		// request.setAttribute("regionList", regionList);

		List<Region> regionList = regionService.findByProvince();
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
