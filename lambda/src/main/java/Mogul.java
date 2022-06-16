import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mogul {
    public static void main(String[] args) {
        // 搞一组分类
        List<String> categories = Stream.of("维修", "采购", "保险", "污水").collect(Collectors.toList());
        // 做个容器
        List<Map<String, Object>> list = new ArrayList<>(32);
        // 初始化一套数据
        for (int i = 0; i < 32; i++) {
            String itemName = categories.get(i % categories.size());
            Map<String, Object> item = new HashMap<>(2);
            item.put("ITEM_NAME", itemName);
            item.put("SUM", new BigDecimal("21.4"));
            list.add(item);
        }

        List<Map<String, Object>> result = list.stream()
                .collect(Collectors.groupingBy(item -> item.get("ITEM_NAME"), LinkedHashMap::new, Collectors.toCollection(LinkedList::new)))
                .entrySet()
                .stream()
                .map(entry -> {
                    LinkedList<Map<String, Object>> subItems = entry.getValue();
                    Map<String, Object> subtotal = new HashMap<>(2);
                    subtotal.put("ITEM_NAME", entry.getKey() + "小计");
                    double sum = subItems.stream().mapToDouble(item -> ((BigDecimal) item.get("SUM")).doubleValue()).sum();
                    subtotal.put("合计", sum);
                    subItems.add(subtotal);
                    return subItems;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        result.forEach(item -> System.out.println(JSONObject.toJSONString(item)));
    }



//    我是先把[{itemname,sum}]这个数组结构转成了键值对结构，
//    {
//        分类一:[{itemname,sum}],
//        分类二:[{itemname,sum}],
//        分类三:[{itemname,sum}]
//    }
//，再遍历这个结构根据key生成小计的name，根据value小计的生成总和然后头插到value的数链表里，再连成一个数组
//
//
//    循环处理数据的时候你得对数据的结构有一个比较清晰地认知，选择合适的容器。
//
//    确切地说是合适的数据结构
//
//    只不过steam流式处理可以一行写完，实际是可以拆开一步一步写的


}
