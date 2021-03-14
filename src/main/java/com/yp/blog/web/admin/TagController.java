package com.yp.blog.web.admin;

import com.yp.blog.pojo.Tag;
import com.yp.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@RequestMapping("/admin/tags")
@Controller
public class TagController {
    @Autowired
    TagService tagService;
    //进行分页处理后将数据保存在model中 之后再tags.html中进行显示
    @GetMapping
    public String tagPage(@PageableDefault(size = 3,page = 0) Pageable pageable, Model model) {
        Page<Tag> tagPage = tagService.listTag(pageable);
        model.addAttribute("tagPage", tagPage);
        return "admin/tags";
    }
    //tags.html页面中的新增按钮触发的方法,进入新增页面
    //这里的model是进行JSR 303校验时，拿到name字段上的检验提示信息使用的
    @GetMapping("/input")
    public String input(Model model) {
        model.addAttribute("tag",new Tag());
        return "admin/tag-input";
    }
    //在tag-input方法中点击提交按钮触发方法
    //bingingResult是tag检验的结果，两者在入参中的位置必须挨着
    @PostMapping
    public String post(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //name不可重复检验
        if (tagService.findByName(tag.getName()) != null) {
            //为校验加过binding手动添加错误信息
            bindingResult.rejectValue("name","nameRepetition","分类已存在,请重新输入");
        }
        //当检验结果中有错误时
        if (bindingResult.hasErrors()) {
            return "admin/tag-input";
        }
        Tag savetag = tagService.saveTag(tag);
        if (savetag == null) {
            //新增失败
            redirectAttributes.addFlashAttribute("message", "新增失败");
        } else {
            //新增成功
            redirectAttributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }
    //编辑按钮触发的方法，这里必须将tag放进Model中，于新增一样，进入tag-input页面之前必须有tag
    //引入我们在引入name校验时需要从tag中拿到属性name
    @GetMapping("/input/{id}")
    public String editor(@PathVariable Long id,Model model) {
        Tag tag = tagService.getTag(id);
        model.addAttribute("tag",tag);
        return "admin/tag-input";

    }
    //编辑之后点击提交按钮触发的方法
    @PostMapping("/{id}")
    public String editorPost(@PathVariable Long id,@Valid Tag tag, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //name不可重复检验
        if (tagService.findByName(tag.getName()) != null) {
            //为校验加过binding手动添加错误信息
            bindingResult.rejectValue("name","nameRepetition","分类已存在,请重新输入");
        }
        //当检验结果中有错误时
        if (bindingResult.hasErrors()) {
            return "admin/tag-input";
        }
        Tag savetag = tagService.updateTag(id, tag);
        if (savetag == null) {
            //更新失败
            redirectAttributes.addFlashAttribute("message", "更新失败");
        } else {
            //更新成功
            redirectAttributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }
    //删除操作
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes) {
        tagService.deleteTag(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
