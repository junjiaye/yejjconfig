package io.github.junjiaye.yejjconfig.server.dal;

import io.github.junjiaye.yejjconfig.server.model.Configs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: yejjconfig-server
 * @ClassName: ConfigsMapper
 * @description:
 * @author: yejj
 * @create: 2024-05-10 07:36
 */
@Mapper
@Repository
public interface ConfigsMapper {

    @Select("select *from configs where app=#{app} and env = #{env} and ns=#{ns}")
    List<Configs> list(String app, String env, String ns);

    @Select("select *from configs where app=#{app} and env = #{env} and ns=#{ns} and pkey=#{pkey}")
    Configs select(String app, String env, String ns, String pkey);

    @Insert("insert into configs (app,env,ns,pkey,pval) values (#{app},#{env},#{ns},#{pkey},#{pval})")
    void insert(Configs configs);

    @Update("update configs set pval=#{pval} where app=#{app} and env = #{env} and ns=#{ns} and pkey=#{pkey}")
    void update(Configs configs);
}
