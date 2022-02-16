package com.nowcoder.community.dao.es;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

/**
 * 测试es存储和检索帖子数据
 * @author Alex
 * @version 1.0
 * @date 2022/2/16 16:05
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTest {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testInsert(){
        List<DiscussPost> discussPosts = discussPostMapper.selAllDiscussPost();
        discussPostRepository.saveAll(discussPosts);
    }

    @Test
    public void testDelete(){
        discussPostRepository.deleteAll();
    }

    @Test
    public void testSearchRepository(){
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        // DiscussPostRepository底层调用了elasticsearchTemplate.queryForPage()方法,没有把高亮显示的字符和原始查询到的数据合并到一起
        // 底层获取到了高亮显示的值，但是没有返回

        Page<DiscussPost> discussPostPage = discussPostRepository.search(searchQuery);
        System.out.println(discussPostPage.getTotalPages());
        System.out.println(discussPostPage.getTotalElements());
        System.out.println(discussPostPage.getContent());
        System.out.println(discussPostPage.getSize());

        for (DiscussPost discussPost:discussPostPage){
            System.out.println(discussPost);
        }

        System.out.println("==============================================");
//        AggregatedPage<DiscussPost> discussPosts = elasticsearchTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchResultMapper() {
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
//                SearchHits hits = searchResponse.getHits();
//                if (hits.getTotalHits()<=0){
//                    return null;
//                }
//
//                List<DiscussPost> list = new ArrayList<>();
//                for (SearchHit hit : hits) {
//                    DiscussPost post = new DiscussPost();
//                    String id = hit.getSourceAsMap().get("id").toString();
//                    post.setId(Integer.parseInt(id));
//
//                    String userId = hit.getSourceAsMap().get("userId").toString();
//                    post.setUserId(Integer.parseInt(userId));
//
//                    String content = hit.getSourceAsMap().get("content").toString();
//                    post.setContent(content);
//
//                    String title = hit.getSourceAsMap().get("title").toString();
//                    post.setTitle(title);
//
//                    String status = hit.getSourceAsMap().get("status").toString();
//                    post.setStatus(Integer.parseInt(status));
//
//                    String createTime = hit.getSourceAsMap().get("createTime").toString();
//                    post.setCreateTime(new Date(Long.valueOf(createTime)));
//
//                    String commentCount = hit.getSourceAsMap().get("commentCount").toString();
//                    post.setCommentCount(Integer.parseInt(commentCount));
//
//                    // 处理高亮显示的结果
//                    HighlightField titleField = hit.getHighlightFields().get("title");
//                    if(titleField!=null){
//                        post.setTitle(titleField.getFragments()[0].toString());
//                    }
//
//                    HighlightField contentField = hit.getHighlightFields().get("content");
//                    if(contentField!=null){
//                        post.setTitle(contentField.getFragments()[0].toString());
//                    }
//
//                    list.add(post);
//                }
//                return new AggregatedPageImpl(list,pageable,hits.getTotalHits(),
//                        searchResponse.getAggregations(),searchResponse.getScrollId(),hits.getMaxScore());
//            }
//        });
//        for (DiscussPost discussPost : discussPosts) {
//            System.out.println(discussPost);
//        }
    }
}
