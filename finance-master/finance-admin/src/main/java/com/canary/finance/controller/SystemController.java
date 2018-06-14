package com.canary.finance.controller;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.canary.finance.domain.Admin;
import com.canary.finance.domain.Resource;
import com.canary.finance.domain.Role;
import com.canary.finance.pojo.ArrayObjectDTO;
import com.canary.finance.pojo.TreeNodeVO;
import com.canary.finance.service.LogService;
import com.canary.finance.service.SystemService;

@Controller
@RequestMapping("/sys")
public class SystemController extends BaseController {
	@Autowired
	private SystemService systemService;
	@Autowired
	private LogService logService;

	@PostMapping("/operator")
	@ResponseBody
	public Admin getAdmin(String name) {
		return this.systemService.getAdmin(name);
	}
	
	@RequestMapping("/admin/list")
	public String getAdminList(Integer page, Integer size, Model model) {
		int total = this.systemService.getAdminCount();
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("admins", this.systemService.getAdminList(this.getOffset(page, size), this.getPageSize(size)));
		} else {
			model.addAttribute("total", 0);
			model.addAttribute("admins", new Admin[0]);
		}
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "sys/admin/list";
	}
	
	@GetMapping("/admin/add/{page:\\d+}/{size:\\d+}")
	public String addAdmin(@PathVariable("page")int page, @PathVariable("size")int size, Model model) {
		model.addAttribute("roles", this.systemService.getRoleList(1));
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		return "sys/admin/add";
	}
	
	@GetMapping("/admin/{adminId:\\d+}/edit/{page:\\d+}/{size:\\d+}")
	public String editAdmin(@PathVariable("adminId")int adminId, @PathVariable("page")int page, @PathVariable("size")int size, Model model) {
		model.addAttribute("admin", this.systemService.getAdmin(adminId)); 
		model.addAttribute("roles", this.systemService.getRoleList(1));
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		return "sys/admin/edit";
	}
	
	@PostMapping("/admin/save")
	public String saveAdmin(Admin admin, int page, int size, Model model) {
		this.systemService.saveAdmin(admin);
		return this.getAdminList(page, size, model);
	}
	
	@GetMapping("/admin/validate")
	@ResponseBody
	public String validateAdmin(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		Admin admin = this.systemService.getAdmin(fieldValue);
		dto.setObject(fieldId);
		if(admin != null && admin.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	@PostMapping("/admin/{adminId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doAdminOperate(@PathVariable("adminId")int adminId, @PathVariable("operate")int status) {
		Admin admin = this.systemService.getAdmin(adminId);
		if(admin != null && admin.getId() > 0) {
			admin.setStatus(status);
			return this.systemService.saveAdmin(admin);
		}
		return false;
	}
	
	@PostMapping("/admin/{adminId:\\d+}/totp")
	@ResponseBody
	public boolean resetAdminTotp(@PathVariable("adminId")int adminId) {
		Admin admin = this.systemService.getAdmin(adminId);
		if(admin != null && admin.getId() > 0) {
			admin.setTotp(0);
			return this.systemService.saveAdmin(admin);
		}
		return false;
	}
	
	@RequestMapping("/role/list")
	public String getRoleList(Model model) {
		List<Role> list = this.systemService.getRoleList(-1);
		if(list.size() > 0){
			model.addAttribute("roles", list);
		}else{
			model.addAttribute("roles", new Role[0]);
		}
		return "sys/role/list";
	}
	
	@GetMapping("/role/add")
	public String addRole() {
		return "sys/role/add";
	}
	
	@GetMapping("/role/{roleId:\\d+}/edit")
	public String editRole(@PathVariable("roleId")int roleId, Model model) {
		model.addAttribute("role", this.systemService.getRole(roleId)); 
		return "sys/role/edit";
	}
	
	@PostMapping("/role/save")
	public String saveRole(Role role, Model model) {
		this.systemService.saveRole(role);
		return this.getRoleList(model);
	}
	
	@GetMapping("/role/validate")
	@ResponseBody
	public String validateRole(int id, String fieldId, String fieldValue) {
		ArrayObjectDTO dto = new ArrayObjectDTO();
		Role role = this.systemService.getRole(fieldValue);
		dto.setObject(fieldId);
		if(role != null && role.getId() > 0) {
			dto.setSuccess(false);
			dto.setMessage(this.getMessage("name.not.pass"));
		} else {
			dto.setSuccess(true);
			dto.setMessage(this.getMessage("name.pass"));
		}
		return dto.toString(); 
	}
	
	@PostMapping("/role/{roleId:\\d+}/{operate:\\d+}")
	@ResponseBody
	public boolean doRoleOperate(@PathVariable("roleId")int roleId, @PathVariable("operate")int status) {
		Role role = this.systemService.getRole(roleId);
		if(role != null && role.getId() > 0) {
			role.setStatus(status);
			return this.systemService.saveRole(role);
		}
		return false;
	}
	
	@PostMapping("/role/{roleId:\\d+}/resource")
	@ResponseBody
	public List<TreeNodeVO> getRoleResource(@PathVariable("roleId")int roleId) {
		List<TreeNodeVO> resources = new LinkedList<TreeNodeVO>();
		List<Resource> resourceList = this.systemService.getRoleResource(roleId);
		List<Resource> parents = this.systemService.getResourceList();
		if(parents != null && parents.size()>0) {
			for(Resource parent : parents) {
				TreeNodeVO node = new TreeNodeVO();
				node.setId(""+parent.getId());
				node.setName(parent.getName());
				node.setpId(""+parent.getParentId());
				node.setChecked(this.checkRoleResource(resourceList, parent.getId()));
				resources.add(node);
			}
		}
		return resources;
	}
	
	@PostMapping("/role/resource/save")
	@ResponseBody
	public boolean saveRoleResource(int roleId, String resourceIds) {
		if(roleId > 0 && StringUtils.isNotBlank(resourceIds)) {
			return this.systemService.saveRoleResources(roleId, resourceIds);
		} 
		return false;
	}
	
	@RequestMapping("/log/login")
	public String getLoginLogList(String name, String beginTime, String endTime, Integer page, Integer size, Model model) {
		int total = this.logService.getLoginLogCount(name, beginTime, endTime);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("logs", this.logService.getLoginLogList(name, beginTime, endTime, this.getOffset(page, size), this.getPageSize(size)));
		}else{
			model.addAttribute("total", 0);
			model.addAttribute("logs", new com.canary.finance.domain.LogLogin[0]);
		}
		model.addAttribute("name", StringUtils.trimToEmpty(name));
		model.addAttribute("beginTime", StringUtils.trimToEmpty(beginTime));
		model.addAttribute("endTime", StringUtils.trimToEmpty(endTime));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "sys/log/login";
	}
	
	@RequestMapping("/log/operation")
	public String getOperationLogList(String name, String beginTime, String endTime, Integer page, Integer size, Model model) {
		int total = this.logService.getOperationLogCount(name, beginTime, endTime);
		if(total > 0){
			model.addAttribute("total", total);
			model.addAttribute("logs", this.logService.getOperationLogList(name, beginTime, endTime,  this.getOffset(page, size), this.getPageSize(size)));
		}else{
			model.addAttribute("total", 0);
			model.addAttribute("logs", new com.canary.finance.domain.LogOperation[0]);
		}
		model.addAttribute("name", StringUtils.trimToEmpty(name));
		model.addAttribute("beginTime", StringUtils.trimToEmpty(beginTime));
		model.addAttribute("endTime", StringUtils.trimToEmpty(endTime));
		model.addAttribute("size", this.getPageSize(size));
		model.addAttribute("page", this.getPage(page));
		model.addAttribute("pages", this.getTotalPage(total, size));
		return "sys/log/operation";
	}
	
	private boolean checkRoleResource(List<Resource> resourceList, int resourceId) {
		if(resourceList != null && resourceList.size() > 0) {
			for(Resource resource : resourceList){
				if(resource.getId() == resourceId){
					return true;
				}
			}
		}
		return false;
	}
}
