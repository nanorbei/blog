package com.yp.blog.web.admin;

import com.yp.blog.pojo.Type;
import com.yp.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@RequestMapping("/admin/types")
@Controller
public class TypeController {
    @Autowired
    TypeService typeService;
    //进行分页处理后将数据保存在model中 之后再types.html中进行显示
    @GetMapping
    public String typePage(@PageableDefault(size = 3,page = 0) Pageable pageable, Model model) {
        Page<Type> typePage = typeService.listType(pageable);
        model.addAttribute("typePage", typePage);
        return "admin/types";
    }
    //types.html页面中的新增按钮触发的方法,进入新增页面
    //这里的model是进行JSR 303校验时，拿到name字段上的检验提示信息使用的
    @GetMapping("/input")
    public String input(Model model) {
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }
    //在type-input方法中点击提交按钮触发方法
    //bingingResult是type检验的结果，两者在入参中的位置必须挨着
    @PostMapping
    public String post(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //name不可重复检验
        if (typeService.findByName(type.getName()) != null) {
            //为校验加过binding手动添加错误信息
            bindingResult.rejectValue("name","nameRepetition","分类已存在,请重新输入");
        }
        //当检验结果中有错误时
        if (bindingResult.hasErrors()) {
            return "admin/type-input";
        }
        Type saveType = typeService.saveType(type);
        if (saveType == null) {
            //新增失败
            redirectAttributes.addFlashAttribute("message", "新增失败");
        } else {
            //新增成功
            redirectAttributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/types";
    }
    //编辑按钮触发的方法，这里必须将type放进Model中，于新增一样，进入type-input页面之前必须有type
    //引入我们在引入name校验时需要从type中拿到属性name
    @GetMapping("/input/{id}")
    public String editor(@PathVariable Long id,Model model) {
        Type type = typeService.getType(id);
        model.addAttribute("type",type);
        return "admin/type-input";

    }
    //编辑之后点击提交按钮触发的方法
    @PostMapping("/{id}")
    public String editorPost(@PathVariable Long id,@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //name不可重复检验
        if (typeService.findByName(type.getName()) != null) {
            //为校验加过binding手动添加错误信息
            bindingResult.rejectValue("name","nameRepetition","分类已存在,请重新输入");
        }
        //当检验结果中有错误时
        if (bindingResult.hasErrors()) {
            return "admin/type-input";
        }
        Type saveType = typeService.updateType(id, type);
        if (saveType == null) {
            //更新失败
            redirectAttributes.addFlashAttribute("message", "更新失败");
        } else {
            //更新成功
            redirectAttributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }
    //删除操作
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes) {
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }
}
