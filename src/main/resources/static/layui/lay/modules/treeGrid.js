/**
 @Name锛歭ayui.table 琛ㄦ牸鎿嶄綔
 @Author锛氳搐蹇�
 @License锛歁IT
 */
layui.define(['laytpl', 'laypage', 'layer', 'form'], function(exports){
    "use strict";

    var $ = layui.$
        ,laytpl = layui.laytpl
        ,laypage = layui.laypage
        ,layer = layui.layer
        ,form = layui.form
        ,hint = layui.hint()
        ,device = layui.device()

        //澶栭儴鎺ュ彛
        ,table = {
            config: {
                checkName: 'LAY_CHECKED' //鏄惁閫変腑鐘舵€佺殑瀛楁鍚�
                ,indexName: 'LAY_TABLE_INDEX' //涓嬫爣绱㈠紩鍚�
            } //鍏ㄥ眬閰嶇疆椤�
            ,cache: {} //鏁版嵁缂撳瓨
            ,index: layui.table ? (layui.table.index + 10000) : 0

            //璁剧疆鍏ㄥ眬椤�
            ,set: function(options){
                var that = this;
                that.config = $.extend({}, that.config, options);
                return that;
            }

            //浜嬩欢鐩戝惉
            ,on: function(events, callback){
                return layui.onevent.call(this, MOD_NAME, events, callback);
            }
        }

        //鎿嶄綔褰撳墠瀹炰緥
        ,thisTable = function(){
            var that = this
                ,options = that.config
                ,id = options.id;

            id && (thisTable.config[id] = options);

            return {
                reload: function(options){
                    that.reload.call(that, options);
                }
                ,config: options
            }
        }

        //瀛楃甯搁噺
        ,MOD_NAME = 'treeGrid', ELEM = '.layui-table', THIS = 'layui-this', SHOW = 'layui-show', HIDE = 'layui-hide', DISABLED = 'layui-disabled', NONE = 'layui-none'
        ,ELEM_VIEW = 'layui-table-view', ELEM_HEADER = '.layui-table-header', ELEM_BODY = '.layui-table-body', ELEM_MAIN = '.layui-table-main', ELEM_FIXED = '.layui-table-fixed', ELEM_FIXL = '.layui-table-fixed-l', ELEM_FIXR = '.layui-table-fixed-r', ELEM_TOOL = '.layui-table-tool', ELEM_PAGE = '.layui-table-page', ELEM_SORT = '.layui-table-sort', ELEM_EDIT = 'layui-table-edit', ELEM_HOVER = 'layui-table-hover'

        //thead鍖哄煙妯℃澘
        ,TPL_HEADER = function(options){
            var rowCols = '{{#if(item2.colspan){}} colspan="{{item2.colspan}}"{{#} if(item2.rowspan){}} rowspan="{{item2.rowspan}}"{{#}}}';

            options = options || {};
            return ['<table cellspacing="0" cellpadding="0" border="0" class="layui-table" '
                ,'{{# if(d.data.skin){ }}lay-skin="{{d.data.skin}}"{{# } }} {{# if(d.data.size){ }}lay-size="{{d.data.size}}"{{# } }} {{# if(d.data.even){ }}lay-even{{# } }}>'
                ,'<thead>'
                ,'{{# layui.each(d.data.cols, function(i1, item1){ }}'
                ,'<tr>'
                ,'{{# layui.each(item1, function(i2, item2){ }}'
                ,'{{# if(item2.fixed && item2.fixed !== "right"){ left = true; } }}'
                ,'{{# if(item2.fixed === "right"){ right = true; } }}'
                ,function(){
                    if(options.fixed && options.fixed !== 'right'){
                        return '{{# if(item2.fixed && item2.fixed !== "right"){ }}';
                    }
                    if(options.fixed === 'right'){
                        return '{{# if(item2.fixed === "right"){ }}';
                    }
                    return '';
                }()
                ,'<th data-field="{{ item2.field||i2 }}" {{# if(item2.minWidth){ }}data-minwidth="{{item2.minWidth}}"{{# } }} '+ rowCols +' {{# if(item2.unresize){ }}data-unresize="true"{{# } }}>'
                ,'<div class="layui-table-cell laytable-cell-'
                ,'{{# if(item2.colspan > 1){ }}'
                ,'group'
                ,'{{# } else { }}'
                ,'{{d.index}}-{{item2.field || i2}}'
                ,'{{# if(item2.type !== "normal"){ }}'
                ,' laytable-cell-{{ item2.type }}'
                ,'{{# } }}'
                ,'{{# } }}'
                ,'" {{#if(item2.align){}}align="{{item2.align}}"{{#}}}>'
                ,'{{# if(item2.type === "checkbox"){ }}' //澶嶉€夋
                ,'<input type="checkbox" name="layTableCheckbox" lay-skin="primary" lay-filter="layTableAllChoose" {{# if(item2[d.data.checkName]){ }}checked{{# }; }}>'
                ,'{{# } else { }}'
                ,'<span>{{item2.title||""}}</span>'
                ,'{{# if(!(item2.colspan > 1) && item2.sort){ }}'
                ,'<span class="layui-table-sort layui-inline"><i class="layui-edge layui-table-sort-asc"></i><i class="layui-edge layui-table-sort-desc"></i></span>'
                ,'{{# } }}'
                ,'{{# } }}'
                ,'</div>'
                ,'</th>'
                ,(options.fixed ? '{{# }; }}' : '')
                ,'{{# }); }}'
                ,'</tr>'
                ,'{{# }); }}'
                ,'</thead>'
                ,'</table>'].join('');
        }

        //tbody鍖哄煙妯℃澘
        ,TPL_BODY = ['<table cellspacing="0" cellpadding="0" border="0" class="layui-table" '
            ,'{{# if(d.data.skin){ }}lay-skin="{{d.data.skin}}"{{# } }} {{# if(d.data.size){ }}lay-size="{{d.data.size}}"{{# } }} {{# if(d.data.even){ }}lay-even{{# } }}>'
            ,'<tbody></tbody>'
            ,'</table>'].join('')

        //涓绘ā鏉�
        ,TPL_MAIN = ['<div class="layui-form layui-border-box {{d.VIEW_CLASS}}" lay-filter="LAY-table-{{d.index}}" style="{{# if(d.data.width){ }}width:{{d.data.width}}px;{{# } }} {{# if(d.data.height){ }}height:{{d.data.height}}px;{{# } }}">'

            ,'{{# if(d.data.toolbar){ }}'
            ,'<div class="layui-table-tool"></div>'
            ,'{{# } }}'

            ,'<div class="layui-table-box">'
            ,'{{# var left, right; }}'
            ,'<div class="layui-table-header">'
            ,TPL_HEADER()
            ,'</div>'
            ,'<div class="layui-table-body layui-table-main">'
            ,TPL_BODY
            ,'</div>'

            ,'{{# if(left){ }}'
            ,'<div class="layui-table-fixed layui-table-fixed-l">'
            ,'<div class="layui-table-header">'
            ,TPL_HEADER({fixed: true})
            ,'</div>'
            ,'<div class="layui-table-body">'
            ,TPL_BODY
            ,'</div>'
            ,'</div>'
            ,'{{# }; }}'

            ,'{{# if(right){ }}'
            ,'<div class="layui-table-fixed layui-table-fixed-r">'
            ,'<div class="layui-table-header">'
            ,TPL_HEADER({fixed: 'right'})
            ,'<div class="layui-table-mend"></div>'
            ,'</div>'
            ,'<div class="layui-table-body">'
            ,TPL_BODY
            ,'</div>'
            ,'</div>'
            ,'{{# }; }}'
            ,'</div>'

            ,'{{# if(d.data.page){ }}'
            ,'<div class="layui-table-page">'
            ,'<div id="layui-table-page{{d.index}}"></div>'
            ,'</div>'
            ,'{{# } }}'

            ,'<style>'
            ,'{{# layui.each(d.data.cols, function(i1, item1){'
            ,'layui.each(item1, function(i2, item2){ }}'
            ,'.laytable-cell-{{d.index}}-{{item2.field||i2}}{ '
            ,'{{# if(item2.width){ }}'
            ,'width: {{item2.width}}px;'
            ,'{{# } }}'
            ,' }'
            ,'{{# });'
            ,'}); }}'
            ,'</style>'
            ,'</div>'].join('')

        ,_WIN = $(window)
        ,_DOC = $(document)

        //鏋勯€犲櫒
        ,Class = function(options){
            var that = this;
            that.index = ++table.index;
            that.config = $.extend({}, that.config, table.config, options);
            that.render();
        };

    //榛樿閰嶇疆
    Class.prototype.config = {
        limit: 10 //姣忛〉鏄剧ず鐨勬暟閲�
        ,loading: true //璇锋眰鏁版嵁鏃讹紝鏄惁鏄剧ずloading
        ,cellMinWidth: 60 //鎵€鏈夊崟鍏冩牸榛樿鏈€灏忓搴�
        ,text: {
            none: '鏃犳暟鎹�'
        }
    };

    //琛ㄦ牸娓叉煋
    Class.prototype.render = function(){
        var that = this
            ,options = that.config;

        options.elem = $(options.elem);
        options.where = options.where || {};
        options.id = options.id || options.elem.attr('id');

        //璇锋眰鍙傛暟鐨勮嚜瀹氫箟鏍煎紡
        options.request = $.extend({
            pageName: 'page'
            ,limitName: 'limit'
        }, options.request)

        //鍝嶅簲鏁版嵁鐨勮嚜瀹氫箟鏍煎紡
        options.response = $.extend({
            statusName: 'code'
            ,statusCode: 0
            ,msgName: 'msg'
            ,dataName: 'data'
            ,countName: 'count'
        }, options.response);

        //濡傛灉 page 浼犲叆 laypage 瀵硅薄
        if(typeof options.page === 'object'){
            options.limit = options.page.limit || options.limit;
            options.limits = options.page.limits || options.limits;
            that.page = options.page.curr = options.page.curr || 1;
            delete options.page.elem;
            delete options.page.jump;
        }

        if(!options.elem[0]) return that;

        that.setArea(); //鍔ㄦ€佸垎閰嶅垪瀹介珮

        //寮€濮嬫彃鍏ユ浛浠ｅ厓绱�
        var othis = options.elem
            ,hasRender = othis.next('.' + ELEM_VIEW)

            //涓诲鍣�
            ,reElem = that.elem = $(laytpl(TPL_MAIN).render({
                VIEW_CLASS: ELEM_VIEW
                ,data: options
                ,index: that.index //绱㈠紩
            }));

        options.index = that.index;

        //鐢熸垚鏇夸唬鍏冪礌
        hasRender[0] && hasRender.remove(); //濡傛灉宸茬粡娓叉煋锛屽垯Rerender
        othis.after(reElem);

        //鍚勭骇瀹瑰櫒
        that.layHeader = reElem.find(ELEM_HEADER);
        that.layMain = reElem.find(ELEM_MAIN);
        that.layBody = reElem.find(ELEM_BODY);
        that.layFixed = reElem.find(ELEM_FIXED);
        that.layFixLeft = reElem.find(ELEM_FIXL);
        that.layFixRight = reElem.find(ELEM_FIXR);
        that.layTool = reElem.find(ELEM_TOOL);
        that.layPage = reElem.find(ELEM_PAGE);

        that.layTool.html(
            laytpl($(options.toolbar).html()||'').render(options)
        );

        if(options.height) that.fullSize(); //璁剧疆body鍖哄煙楂樺害

        //濡傛灉澶氱骇琛ㄥご锛屽垯濉ˉ琛ㄥご楂樺害
        if(options.cols.length > 1){
            var th = that.layFixed.find(ELEM_HEADER).find('th');
            th.height(that.layHeader.height() - 1 - parseFloat(th.css('padding-top')) - parseFloat(th.css('padding-bottom')));
        }

        //璇锋眰鏁版嵁
        that.pullData(that.page);
        that.events();
    };

    //鏍规嵁鍒楃被鍨嬶紝瀹氬埗鍖栧弬鏁�
    Class.prototype.initOpts = function(item){
        var that = this,
            options = that.config
            ,initWidth = {
                checkbox: 48
                ,space: 15
                ,numbers: 40
            };

        //璁� type 鍙傛暟鍏煎鏃х増鏈�
        if(item.checkbox) item.type = "checkbox";
        if(item.space) item.type = "space";
        if(!item.type) item.type = "normal";

        if(item.type !== "normal"){
            item.unresize = true;
            item.width = item.width || initWidth[item.type];
        }
    };

    //鍔ㄦ€佸垎閰嶅垪瀹介珮
    Class.prototype.setArea = function(){
        var that = this,
            options = that.config
            ,colNums = 0 //鍒椾釜鏁�
            ,autoColNums = 0 //鑷姩鍒楀鐨勫垪涓暟
            ,autoWidth = 0 //鑷姩鍒楀垎閰嶇殑瀹藉害
            ,countWidth = 0 //鎵€鏈夊垪鎬诲搴﹀拰
            ,cntrWidth = options.width || function(){ //鑾峰彇瀹瑰櫒瀹藉害
                //濡傛灉鐖跺厓绱犲搴︿负0锛堜竴鑸负闅愯棌鍏冪礌锛夛紝鍒欑户缁煡鎵句笂灞傚厓绱狅紝鐩村埌鎵惧埌鐪熷疄瀹藉害涓烘
                var getWidth = function(parent){
                    var width, isNone;
                    parent = parent || options.elem.parent()
                    width = parent.width();
                    try {
                        isNone = parent.css('display') === 'none';
                    } catch(e){}
                    if(parent[0] && (!width || isNone)) return getWidth(parent.parent());
                    return width;
                };
                return getWidth();
            }();

        //缁熻鍒椾釜鏁�
        that.eachCols(function(){
            colNums++;
        });

        //鍑忓幓杈规宸�
        cntrWidth = cntrWidth - function(){
            return (options.skin === 'line' || options.skin === 'nob') ? 2 : colNums + 1;
        }();

        //閬嶅巻鎵€鏈夊垪
        layui.each(options.cols, function(i1, item1){
            layui.each(item1, function(i2, item2){
                var width;

                if(!item2){
                    item1.splice(i2, 1);
                    return;
                }

                that.initOpts(item2);
                width = item2.width || 0;

                if(item2.colspan > 1) return;

                if(/\d+%$/.test(width)){
                    item2.width = width = Math.floor((parseFloat(width) / 100) * cntrWidth);
                } else if(!width){ //鍒楀鏈～鍐�
                    item2.width = width = 0;
                    autoColNums++;
                }

                countWidth = countWidth + width;
            });
        });

        that.autoColNums = autoColNums; //璁板綍鑷姩鍒楁暟

        //濡傛灉鏈～鍏呮弧锛屽垯灏嗗墿浣欏搴﹀钩鍒嗐€傚惁鍒欙紝缁欐湭璁惧畾瀹藉害鐨勫垪璧嬪€间竴涓粯璁ゅ
        (cntrWidth > countWidth && autoColNums) && (
            autoWidth = (cntrWidth - countWidth) / autoColNums
        );

        layui.each(options.cols, function(i1, item1){
            layui.each(item1, function(i2, item2){
                var minWidth = item2.minWidth || options.cellMinWidth;
                if(item2.colspan > 1) return;
                if(item2.width === 0){
                    item2.width = Math.floor(autoWidth >= minWidth ? autoWidth : minWidth); //涓嶈兘浣庝簬璁惧畾鐨勬渶灏忓搴�
                }
            });
        });

        //楂樺害閾烘弧锛歠ull-宸窛鍊�
        if(options.height && /^full-\d+$/.test(options.height)){
            that.fullHeightGap = options.height.split('-')[1];
            options.height = _WIN.height() - that.fullHeightGap;
        }
    };

    //琛ㄦ牸閲嶈浇
    Class.prototype.reload = function(options){
        var that = this;
        if(that.config.data && that.config.data.constructor === Array) delete that.config.data;
        that.config = $.extend({}, that.config, options);
        that.render();
    };

    //椤电爜
    Class.prototype.page = 1;

    //鑾峰緱鏁版嵁
    Class.prototype.pullData = function(curr, loadIndex){
        var that = this
            ,options = that.config
            ,request = options.request
            ,response = options.response
            ,sort = function(){
            if(typeof options.initSort === 'object'){
                that.sort(options.initSort.field, options.initSort.type);
            }
        };

        that.startTime = new Date().getTime(); //娓叉煋寮€濮嬫椂闂�

        if(options.url){ //Ajax璇锋眰
            var params = {};
            params[request.pageName] = curr;
            params[request.limitName] = options.limit;

            $.ajax({
                type: options.method || 'get'
                ,url: options.url
                ,data: $.extend(params, options.where)
                ,dataType: 'json'
                ,success: function(res){
                    var data = that.filterArray(res.data,options.treeId,options.treeUpId);
                    res.data=data[0];

                    if(res[response.statusName] != response.statusCode){
                        that.renderForm();
                        that.layMain.html('<div class="'+ NONE +'">'+ (res[response.msgName] || '杩斿洖鐨勬暟鎹姸鎬佸紓甯�') +'</div>');
                    } else {
                        that.renderData(res, curr, res[response.countName]), sort();
                        options.time = (new Date().getTime() - that.startTime) + ' ms'; //鑰楁椂锛堟帴鍙ｈ姹�+瑙嗗浘娓叉煋锛�
                    }
                    loadIndex && layer.close(loadIndex);
                    typeof options.done === 'function' && options.done(res, curr, res[response.countName]);
                }
                ,error: function(e, m){
                    that.layMain.html('<div class="'+ NONE +'">鏁版嵁鎺ュ彛璇锋眰寮傚父</div>');
                    that.renderForm();
                    loadIndex && layer.close(loadIndex);
                }
            });
        } else if(options.data && options.data.constructor === Array){ //宸茬煡鏁版嵁
            var res = {}
                ,startLimit = curr*options.limit - options.limit

            // console.log(options.data,options.treeId,options.treeUpId);

            var data = that.filterArray(options.data,options.treeId,options.treeUpId);
            res.data=data[0];

            res[response.dataName] = res.data;//.concat().splice(startLimit, options.limit);
            res[response.countName] = res.data.length;

            // console.log(res.data);

            that.renderData(res, curr, options.data.length), sort();
            typeof options.done === 'function' && options.done(res, curr, res[response.countName]);
        }
    };

    //閬嶅巻琛ㄥご
    Class.prototype.eachCols = function(callback){
        var cols = $.extend(true, [], this.config.cols)
            ,arrs = [], index = 0;

        //閲嶆柊鏁寸悊琛ㄥご缁撴瀯
        layui.each(cols, function(i1, item1){
            layui.each(item1, function(i2, item2){
                //濡傛灉鏄粍鍚堝垪锛屽垯鎹曡幏瀵瑰簲鐨勫瓙鍒�
                if(item2.colspan > 1){
                    var childIndex = 0;
                    index++
                    item2.CHILD_COLS = [];
                    layui.each(cols[i1 + 1], function(i22, item22){
                        if(item22.PARENT_COL || childIndex == item2.colspan) return;
                        item22.PARENT_COL = index;
                        item2.CHILD_COLS.push(item22);
                        childIndex = childIndex + (item22.colspan > 1 ? item22.colspan : 1);
                    });
                }
                if(item2.PARENT_COL) return; //濡傛灉鏄瓙鍒楋紝鍒欎笉杩涜杩藉姞锛屽洜涓哄凡缁忓瓨鍌ㄥ湪鐖跺垪涓�
                arrs.push(item2)
            });
        });

        //閲嶆柊閬嶅巻鍒楋紝濡傛灉鏈夊瓙鍒楋紝鍒欒繘鍏ラ€掑綊
        var eachArrs = function(obj){
            layui.each(obj || arrs, function(i, item){
                if(item.CHILD_COLS) return eachArrs(item.CHILD_COLS);
                callback(i, item);
            });
        };

        eachArrs();
    };

    /**
     * 灏嗗垪琛ㄦ暟鎹浆鎴愭爲褰㈢粨鏋勫拰绗﹀悎table灞曠ず鐨勫垪琛�
     * @param data          鍒楄〃鏁版嵁
     * @param field_Id      鏍戝舰缁撴瀯涓婚敭瀛楁
     * @param field_upId    鏍戝舰缁撴瀯涓婄骇瀛楁
     * @returns {Array}     [0]琛ㄦ牸鍒楄〃  [1]鏍戝舰缁撴瀯
     */
    Class.prototype.filterArray=function(data,field_Id,field_upId) {
        var list=[];
        var treeList=[];
        var tableList=[];

        // console.log(data,field_Id,field_upId);

        //璁剧疆榛樿鍙傛暟
        if (data){
            for (var i = 0; i < data.length; i++) {
                var n = data[i];
                n.isOpen=true;
            }

            //澶勭悊鏍戠粨鏋�
            var fa = function(upId) {
                var _array = [];
                for (var i = 0; i < data.length; i++) {
                    var n = data[i];
                    if (n[field_upId] === upId) {
                        n.children = fa(n[field_Id]);
                        _array.push(n);
                    }
                }
                return _array;
            }
            treeList=fa(data[0][field_upId],"");//閫掑綊

        }

        //澶勭悊琛ㄦ牸缁撴瀯
        var fa2=function (l,level,upids) {
            for (var i = 0; i < l.length; i++) {
                var n = l[i];
                n.level=level;//璁剧疆褰撳墠灞傜骇
                n.upIds=upids;
                tableList.push(n);
                if (n.children&&n.children.length>0) {
                    fa2(n.children,1+level,upids+"_"+n[field_Id]+"_");
                }
            }
            return;
        }
        fa2(treeList,1,"");

        list.push(tableList);//table缁撴瀯
        list.push(treeList)//tree鏍戠粨鏋�
        return list;
    }

    //鏁版嵁娓叉煋
    Class.prototype.renderData = function(res, curr, count, sort){
        var that = this
            ,options = that.config
            ,data = res[options.response.dataName] || []
            ,trs = []
            ,trs_fixed = []
            ,trs_fixed_r = []

            //娓叉煋瑙嗗浘
            ,render = function(){ //鍚庣画鎬ц兘鎻愬崌鐨勯噸鐐�
                if(!sort && that.sortKey){
                    return that.sort(that.sortKey.field, that.sortKey.sort, true);
                }
                layui.each(data, function(i1, item1){
                    var tds = [], tds_fixed = [], tds_fixed_r = []
                        ,numbers = i1 + options.limit*(curr - 1) + 1; //搴忓彿

                    if(item1.length === 0) return;
                    if(!sort){
                        item1[table.config.indexName] = i1;
                    }

                    that.eachCols(function(i3, item3){
                        var field = item3.field || i3, content = item1[field]
                            ,cell = that.getColElem(that.layHeader, field);
                        if(content === undefined || content === null) content = '';
                        if(item3.colspan > 1) return;
                        var o=data[i1];

                        var treeImgHtml='';//鏍戝舰鍥炬爣
                        var treeShowName=options.treeShowName;//鏄剧ず鍊�
                        if(treeShowName==item3.field){//褰撳墠鏄惁鐢ㄤ簬鏄剧ず鐨勫€�
                            treeImgHtml+='<div style="float: left;height: 28px;line-height: 28px;padding-left: 5px;">';
                            var temTreeHtml='<i class="layui-icon layui-tree-head">&#xe625;</i> ';

                            var nbspHtml="<i>"//涓€娆′綅绉�
                            for(var i=1;i<o.level;i++) {
                                nbspHtml = nbspHtml + "&nbsp;&nbsp;&nbsp;&nbsp;";
                            }
                            nbspHtml=nbspHtml+"</i>";

                            if(o.children&&o.children.length>0){//闈炲彾瀛愯妭鐐�
                                treeImgHtml+=nbspHtml+temTreeHtml;
                            }else{
                                treeImgHtml+=nbspHtml+'<i class="layui-icon layui-tree-head">&nbsp;&nbsp;</i> ';
                            }

                            treeImgHtml+="</div>";
                        }

                        //td鍐呭
                        var td = ['<td data-field="'+ field +'" '+ function(){
                            var attr = [];
                            if(item3.edit) attr.push('data-edit="'+ item3.edit +'"'); //鏄惁鍏佽鍗曞厓鏍肩紪杈�
                            if(item3.align) attr.push('align="'+ item3.align +'"'); //瀵归綈鏂瑰紡
                            if(item3.templet) attr.push('data-content="'+ content +'"'); //鑷畾涔夋ā鏉�
                            if(item3.toolbar) attr.push('data-off="true"'); //鑷畾涔夋ā鏉�
                            if(item3.event) attr.push('lay-event="'+ item3.event +'"'); //鑷畾涔変簨浠�
                            if(item3.style) attr.push('style="'+ item3.style +'"'); //鑷畾涔夋牱寮�
                            if(item3.minWidth) attr.push('data-minwidth="'+ item3.minWidth +'"'); //鍗曞厓鏍兼渶灏忓搴�
                            return attr.join(' ');
                        }() +'>'
                            ,'<div class="layui-table-cell laytable-cell-'+ function(){ //杩斿洖瀵瑰簲鐨凜SS绫绘爣璇�
                                var str = (options.index + '-' + field);
                                return item3.type === 'normal' ? str
                                    : (str + ' laytable-cell-' + item3.type);
                            }() +'">'+treeImgHtml+'<p style="width: auto;height: 100%;">'+function(){
                                var tplData = $.extend(true, {
                                    LAY_INDEX: numbers
                                }, item1);

                                //娓叉煋澶嶉€夋鍒楄鍥�
                                if(item3.type === 'checkbox'){
                                    return '<input type="checkbox" name="layTableCheckbox" lay-skin="primary" '+ function(){
                                        var checkName = table.config.checkName;
                                        //濡傛灉鏄叏閫�
                                        if(item3[checkName]){
                                            item1[checkName] = item3[checkName];
                                            return item3[checkName] ? 'checked' : '';
                                        }
                                        return tplData[checkName] ? 'checked' : '';
                                    }() +'>';
                                } else if(item3.type === 'numbers'){ //娓叉煋搴忓彿
                                    return numbers;
                                }

                                //瑙ｆ瀽宸ュ叿鍒楁ā鏉�
                                if(item3.toolbar){
                                    return laytpl($(item3.toolbar).html()||'').render(tplData);
                                }
                                return item3.templet ? function(){
                                    return typeof item3.templet === 'function'
                                        ? item3.templet(tplData)
                                        : laytpl($(item3.templet).html() || String(content)).render(tplData)
                                }() : content;
                            }()
                            ,' </p></div></td>'].join('');

                        tds.push(td);
                        if(item3.fixed && item3.fixed !== 'right') tds_fixed.push(td);
                        if(item3.fixed === 'right') tds_fixed_r.push(td);
                    });

                    trs.push('<tr data-index="'+ i1 +'" upids="'+item1["upIds"]+'">'+ tds.join('') + '</tr>');
                    trs_fixed.push('<tr data-index="'+ i1 +'" upids="'+item1["upIds"]+'">'+ tds_fixed.join('') + '</tr>');
                    trs_fixed_r.push('<tr data-index="'+ i1 +'" upids="'+item1["upIds"]+'">'+ tds_fixed_r.join('') + '</tr>');
                });

                //if(data.length === 0) return;

                that.layBody.scrollTop(0);
                that.layMain.find('.'+ NONE).remove();
                that.layMain.find('tbody').html(trs.join(''));
                that.layFixLeft.find('tbody').html(trs_fixed.join(''));
                that.layFixRight.find('tbody').html(trs_fixed_r.join(''));

                that.renderForm();
                that.syncCheckAll();
                that.haveInit ? that.scrollPatch() : setTimeout(function(){
                    that.scrollPatch();
                }, 50);
                that.haveInit = true;
                layer.close(that.tipsIndex);
            };

        that.key = options.id || options.index;
        table.cache[that.key] = data; //璁板綍鏁版嵁

        //鏄剧ず闅愯棌鍒嗛〉鏍�
        that.layPage[data.length === 0 && curr == 1 ? 'addClass' : 'removeClass'](HIDE);

        //鎺掑簭
        if(sort){
            return render();
        }

        if(data.length === 0){
            that.renderForm();
            that.layFixed.remove();
            that.layMain.find('tbody').html('');
            that.layMain.find('.'+ NONE).remove();
            return that.layMain.append('<div class="'+ NONE +'">'+ options.text.none +'</div>');
        }

        render();

        //鍚屾鍒嗛〉鐘舵€�
        if(options.page){
            options.page = $.extend({
                elem: 'layui-table-page' + options.index
                ,count: count
                ,limit: options.limit
                ,limits: options.limits || [10,20,30,40,50,60,70,80,90]
                ,groups: 3
                ,layout: ['prev', 'page', 'next', 'skip', 'count', 'limit']
                ,prev: '<i class="layui-icon">&#xe603;</i>'
                ,next: '<i class="layui-icon">&#xe602;</i>'
                ,jump: function(obj, first){
                    if(!first){
                        //鍒嗛〉鏈韩骞堕潪闇€瑕佸仛浠ヤ笅鏇存柊锛屼笅闈㈠弬鏁扮殑鍚屾锛屼富瑕佹槸鍥犱负鍏跺畠澶勭悊缁熶竴鐢ㄥ埌浜嗗畠浠�
                        //鑰屽苟闈炵敤鐨勬槸 options.page 涓殑鍙傛暟锛堜互纭繚鍒嗛〉鏈紑鍚殑鎯呭喌浠嶈兘姝ｅ父浣跨敤锛�
                        that.page = obj.curr; //鏇存柊椤电爜
                        options.limit = obj.limit; //鏇存柊姣忛〉鏉℃暟

                        that.pullData(obj.curr, that.loading());
                    }
                }
            }, options.page);
            options.page.count = count; //鏇存柊鎬绘潯鏁�
            laypage.render(options.page);
        }
    };

    //鎵惧埌瀵瑰簲鐨勫垪鍏冪礌
    Class.prototype.getColElem = function(parent, field){
        var that = this
            ,options = that.config;
        return parent.eq(0).find('.laytable-cell-'+ (options.index + '-' + field) + ':eq(0)');
    };

    //娓叉煋琛ㄥ崟
    Class.prototype.renderForm = function(type){
        form.render(type, 'LAY-table-'+ this.index);
    }

    //鏁版嵁鎺掑簭
    Class.prototype.sort = function(th, type, pull, formEvent){
        var that = this
            ,field
            ,res = {}
            ,options = that.config
            ,filter = options.elem.attr('lay-filter')
            ,data = table.cache[that.key], thisData;

        //瀛楁鍖归厤
        if(typeof th === 'string'){
            that.layHeader.find('th').each(function(i, item){
                var othis = $(this)
                    ,_field = othis.data('field');
                if(_field === th){
                    th = othis;
                    field = _field;
                    return false;
                }
            });
        }

        try {
            var field = field || th.data('field');

            //濡傛灉娆叉墽琛岀殑鎺掑簭宸插湪鐘舵€佷腑锛屽垯涓嶆墽琛屾覆鏌�
            if(that.sortKey && !pull){
                if(field === that.sortKey.field && type === that.sortKey.sort){
                    return;
                }
            }

            var elemSort = that.layHeader.find('th .laytable-cell-'+ options.index +'-'+ field).find(ELEM_SORT);
            that.layHeader.find('th').find(ELEM_SORT).removeAttr('lay-sort'); //娓呴櫎鍏跺畠鏍囬鎺掑簭鐘舵€�
            elemSort.attr('lay-sort', type || null);
            that.layFixed.find('th')
        } catch(e){
            return hint.error('Table modules: Did not match to field');
        }

        //璁板綍鎺掑簭绱㈠紩鍜岀被鍨�
        that.sortKey = {
            field: field
            ,sort: type
        };

        if(type === 'asc'){ //鍗囧簭
            thisData = layui.sort(data, field);
        } else if(type === 'desc'){ //闄嶅簭
            thisData = layui.sort(data, field, true);
        } else { //娓呴櫎鎺掑簭
            thisData = layui.sort(data, table.config.indexName);
            delete that.sortKey;
        }

        res[options.response.dataName] = thisData;
        that.renderData(res, that.page, that.count, true);

        if(formEvent){
            layui.event.call(th, MOD_NAME, 'sort('+ filter +')', {
                field: field
                ,type: type
            });
        }
    };

    //璇锋眰loading
    Class.prototype.loading = function(){
        var that = this
            ,options = that.config;
        if(options.loading && options.url){
            return layer.msg('鏁版嵁璇锋眰涓�', {
                icon: 16
                ,offset: [
                    that.elem.offset().top + that.elem.height()/2 - 35 - _WIN.scrollTop() + 'px'
                    ,that.elem.offset().left + that.elem.width()/2 - 90 - _WIN.scrollLeft() + 'px'
                ]
                ,time: -1
                ,anim: -1
                ,fixed: false
            });
        }
    };

    //鍚屾閫変腑鍊肩姸鎬�
    Class.prototype.setCheckData = function(index, checked){
        var that = this
            ,options = that.config
            ,thisData = table.cache[that.key];
        if(!thisData[index]) return;
        if(thisData[index].constructor === Array) return;
        thisData[index][options.checkName] = checked;
    };

    //鍚屾鍏ㄩ€夋寜閽姸鎬�
    Class.prototype.syncCheckAll = function(){
        var that = this
            ,options = that.config
            ,checkAllElem = that.layHeader.find('input[name="layTableCheckbox"]')
            ,syncColsCheck = function(checked){
            that.eachCols(function(i, item){
                if(item.type === 'checkbox'){
                    item[options.checkName] = checked;
                }
            });
            return checked;
        };

        if(!checkAllElem[0]) return;

        if(table.checkStatus(that.key).isAll){
            if(!checkAllElem[0].checked){
                checkAllElem.prop('checked', true);
                that.renderForm('checkbox');
            }
            syncColsCheck(true);
        } else {
            if(checkAllElem[0].checked){
                checkAllElem.prop('checked', false);
                that.renderForm('checkbox');
            }
            syncColsCheck(false);
        }
    };

    //鑾峰彇cssRule
    Class.prototype.getCssRule = function(field, callback){
        var that = this
            ,style = that.elem.find('style')[0]
            ,sheet = style.sheet || style.styleSheet || {}
            ,rules = sheet.cssRules || sheet.rules;
        layui.each(rules, function(i, item){
            if(item.selectorText === ('.laytable-cell-'+ that.index +'-'+ field)){
                return callback(item), true;
            }
        });
    };

    //閾烘弧琛ㄦ牸涓讳綋楂樺害
    Class.prototype.fullSize = function(){
        var that = this
            ,options = that.config
            ,height = options.height, bodyHeight;

        if(that.fullHeightGap){
            height = _WIN.height() - that.fullHeightGap;
            if(height < 135) height = 135;
            that.elem.css('height', height);
        }

        //tbody鍖哄煙楂樺害
        bodyHeight = parseFloat(height) - parseFloat(that.layHeader.height()) - 1;
        if(options.toolbar){
            bodyHeight = bodyHeight - that.layTool.outerHeight();
        }
        if(options.page){
            bodyHeight = bodyHeight - that.layPage.outerHeight() - 1;
        }
        that.layMain.css('height', bodyHeight);
    };

    //鑾峰彇婊氬姩鏉″搴�
    Class.prototype.getScrollWidth = function(elem){
        var width = 0;
        if(elem){
            width = elem.offsetWidth - elem.clientWidth;
        } else {
            elem = document.createElement('div');
            elem.style.width = '100px';
            elem.style.height = '100px';
            elem.style.overflowY = 'scroll';

            document.body.appendChild(elem);
            width = elem.offsetWidth - elem.clientWidth;
            document.body.removeChild(elem);
        }
        return width;
    };

    //婊氬姩鏉¤ˉ涓�
    Class.prototype.scrollPatch = function(){
        var that = this
            ,layMainTable = that.layMain.children('table')
            ,scollWidth = that.layMain.width() - that.layMain.prop('clientWidth') //绾靛悜婊氬姩鏉″搴�
            ,scollHeight = that.layMain.height() - that.layMain.prop('clientHeight') //妯悜婊氬姩鏉￠珮搴�
            ,getScrollWidth = that.getScrollWidth(that.layMain[0]) //鑾峰彇涓诲鍣ㄦ粴鍔ㄦ潯瀹藉害锛屽鏋滄湁鐨勮瘽
            ,outWidth = layMainTable.outerWidth() - that.layMain.width(); //琛ㄦ牸鍐呭鍣ㄧ殑瓒呭嚭瀹藉害

        //濡傛灉瀛樺湪鑷姩鍒楀锛屽垯瑕佷繚璇佺粷瀵瑰～鍏呮弧锛屽苟涓斾笉鑳藉嚭鐜版í鍚戞粴鍔ㄦ潯
        if(that.autoColNums && outWidth < 5 && !that.scrollPatchWStatus){
            var th = that.layHeader.eq(0).find('thead th:last-child')
                ,field = th.data('field');
            that.getCssRule(field, function(item){
                var width = item.style.width || th.outerWidth();
                item.style.width = (parseFloat(width) - getScrollWidth - outWidth) + 'px';

                //浜屾鏍￠獙锛屽鏋滀粛鐒跺嚭鐜版í鍚戞粴鍔ㄦ潯
                if(that.layMain.height() - that.layMain.prop('clientHeight') > 0){
                    item.style.width = parseFloat(item.style.width) - 1 + 'px';
                }

                that.scrollPatchWStatus = true;
            });
        }

        if(scollWidth && scollHeight){
            if(!that.elem.find('.layui-table-patch')[0]){
                var patchElem = $('<th class="layui-table-patch"><div class="layui-table-cell"></div></th>'); //琛ヤ竵鍏冪礌
                patchElem.find('div').css({
                    width: scollWidth
                });
                that.layHeader.eq(0).find('thead tr').append(patchElem)
            }
        } else {
            that.layHeader.eq(0).find('.layui-table-patch').remove();
        }

        //鍥哄畾鍒楀尯鍩熼珮搴�
        var mainHeight = that.layMain.height()
            ,fixHeight = mainHeight - scollHeight;
        that.layFixed.find(ELEM_BODY).css('height', layMainTable.height() > fixHeight ? fixHeight : 'auto');

        //琛ㄦ牸瀹藉害灏忎簬瀹瑰櫒瀹藉害鏃讹紝闅愯棌鍥哄畾鍒�
        that.layFixRight[outWidth > 0 ? 'removeClass' : 'addClass'](HIDE);

        //鎿嶄綔鏍�
        that.layFixRight.css('right', scollWidth - 1);
    };

    //浜嬩欢澶勭悊
    Class.prototype.events = function(){
        var that = this
            ,options = that.config
            ,_BODY = $('body')
            ,dict = {}
            ,th = that.layHeader.find('th')
            ,resizing
            ,ELEM_CELL = '.layui-table-cell p'
            ,filter = options.elem.attr('lay-filter');

        //鎷栨嫿璋冩暣瀹藉害
        th.on('mousemove', function(e){
            var othis = $(this)
                ,oLeft = othis.offset().left
                ,pLeft = e.clientX - oLeft;
            if(othis.attr('colspan') > 1 || othis.data('unresize') || dict.resizeStart){
                return;
            }
            dict.allowResize = othis.width() - pLeft <= 10; //鏄惁澶勪簬鎷栨嫿鍏佽鍖哄煙
            _BODY.css('cursor', (dict.allowResize ? 'col-resize' : ''));
        }).on('mouseleave', function(){
            var othis = $(this);
            if(dict.resizeStart) return;
            _BODY.css('cursor', '');
        }).on('mousedown', function(e){
            var othis = $(this);
            if(dict.allowResize){
                var field = othis.data('field');
                e.preventDefault();
                dict.resizeStart = true; //寮€濮嬫嫋鎷�
                dict.offset = [e.clientX, e.clientY]; //璁板綍鍒濆鍧愭爣

                that.getCssRule(field, function(item){
                    var width = item.style.width || othis.outerWidth();
                    dict.rule = item;
                    dict.ruleWidth = parseFloat(width);
                    dict.minWidth = othis.data('minwidth') || options.cellMinWidth;
                });
            }
        });
        //鎷栨嫿涓�
        _DOC.on('mousemove', function(e){
            if(dict.resizeStart){
                e.preventDefault();
                if(dict.rule){
                    var setWidth = dict.ruleWidth + e.clientX - dict.offset[0];
                    if(setWidth < dict.minWidth) setWidth = dict.minWidth;
                    dict.rule.style.width = setWidth + 'px';
                    layer.close(that.tipsIndex);
                }
                resizing = 1
            }
        }).on('mouseup', function(e){
            if(dict.resizeStart){
                dict = {};
                _BODY.css('cursor', '');
                that.scrollPatch();
            }
            if(resizing === 2){
                resizing = null;
            }
        });

        //鎺掑簭
        th.on('click', function(){
            var othis = $(this)
                ,elemSort = othis.find(ELEM_SORT)
                ,nowType = elemSort.attr('lay-sort')
                ,type;

            if(!elemSort[0] || resizing === 1) return resizing = 2;

            if(nowType === 'asc'){
                type = 'desc';
            } else if(nowType === 'desc'){
                type = null;
            } else {
                type = 'asc';
            }
            that.sort(othis, type, null, true);
        }).find(ELEM_SORT+' .layui-edge ').on('click', function(e){
            var othis = $(this)
                ,index = othis.index()
                ,field = othis.parents('th').eq(0).data('field')
            layui.stope(e);
            if(index === 0){
                that.sort(field, 'asc', null, true);
            } else {
                that.sort(field, 'desc', null, true);
            }
        });

        /**
         * 鏍戝舰鑺傜偣鐐瑰嚮浜嬩欢锛堥殣钘忓睍寮€涓嬬骇鑺傜偣锛�
         */
        that.elem.on('click', 'i.layui-tree-head', function(){
            var othis = $(this)
                ,index = othis.parents('tr').eq(0).data('index')
                ,tr = that.layBody.find('tr[data-index="'+ index +'"]')
                ,datas=table.cache[that.key];//鏁版嵁
            var o=datas[index];

            var stime=new Date();
            var sonO=$("[upids*=_"+o[options.treeId]+"_]");
            if(o.isOpen){//鎵撳紑鐘舵€佺殑锛屽叧闂�
                sonO.hide();
            }else{
                sonO.show();
            }

            var etime=new Date();
            // console.log((etime-stime)/1000+"绉�");

            o.isOpen=!o.isOpen;//璁剧疆鎵撳紑鐘舵€�

            //澶勭悊鍥炬爣
            var dbClickI=tr.find('.layui-tree-head');
            if(o.isOpen){//鎵撳紑鐘舵€�
                dbClickI.html('&#xe625;');
            }else{
                dbClickI.html('&#xe623;');
            }
        });

        //澶嶉€夋閫夋嫨
        that.elem.on('click', 'input[name="layTableCheckbox"]+', function(){
            var checkbox = $(this).prev()
                ,childs = that.layBody.find('input[name="layTableCheckbox"]')
                ,index = checkbox.parents('tr').eq(0).data('index')
                ,checked = checkbox[0].checked
                ,isAll = checkbox.attr('lay-filter') === 'layTableAllChoose';

            //鍏ㄩ€�
            if(isAll){
                childs.each(function(i, item){
                    item.checked = checked;
                    that.setCheckData(i, checked);
                });
                that.syncCheckAll();
                that.renderForm('checkbox');
            } else {
                that.setCheckData(index, checked);
                that.syncCheckAll();
            }
            layui.event.call(this, MOD_NAME, 'checkbox('+ filter +')', {
                checked: checked
                ,data: table.cache[that.key] ? (table.cache[that.key][index] || {}) : {}
                ,type: isAll ? 'all' : 'one'
            });
        });

        //琛屼簨浠�
        that.layBody.on('mouseenter', 'tr', function(){
            var othis = $(this)
                ,index = othis.index();
            that.layBody.find('tr:eq('+ index +')').addClass(ELEM_HOVER)
        }).on('mouseleave', 'tr', function(){
            var othis = $(this)
                ,index = othis.index();
            that.layBody.find('tr:eq('+ index +')').removeClass(ELEM_HOVER)
        });

        //鍗曞厓鏍肩紪杈�
        that.layBody.on('change', '.'+ELEM_EDIT, function(){
            var othis = $(this)
                ,value = this.value
                ,field = othis.parent().data('field')
                ,index = othis.parents('tr').eq(0).data('index')
                ,data = table.cache[that.key][index];
            data[field] = value; //鏇存柊缂撳瓨涓殑鍊�
            layui.event.call(this, MOD_NAME, 'edit('+ filter +')', {
                value: value
                ,data: data
                ,field: field
            });
        }).on('blur', '.'+ELEM_EDIT, function(){
            var templet
                ,othis = $(this)
                ,field = othis.parent().data('field')
                ,index = othis.parents('tr').eq(0).data('index')
                ,data = table.cache[that.key][index];

            that.eachCols(function(i, item){
                if(item.field == field && item.templet){
                    templet = item.templet;
                }
            });

            othis.parent().find(ELEM_CELL).html(
                templet ? laytpl($(templet).html() || this.value).render(data) : this.value
            );

            othis.parent().data('content', this.value);
            othis.remove();
        });

        //鍗曞厓鏍间簨浠禰td鏀规垚鍗曞厓鏍煎唴瀹圭偣鍑讳簨浠禲
        that.layBody.on('click', 'td div.layui-table-cell p', function(){
            var othis = $(this).parent().parent()
                ,field = othis.data('field')
                ,editType = othis.data('edit')
                ,elemCell = othis.children(ELEM_CELL);
            layer.close(that.tipsIndex);
            if(othis.data('off')) return;

            //鏄剧ず缂栬緫琛ㄥ崟
            if(editType){
                if(editType === 'select') { //閫夋嫨妗�
                    //var select = $('<select class="'+ ELEM_EDIT +'" lay-ignore><option></option></select>');
                    //othis.find('.'+ELEM_EDIT)[0] || othis.append(select);
                } else { //杈撳叆妗�
                    var input = $('<input class="layui-input '+ ELEM_EDIT +'">');
                    input[0].value = $(this).text();//  othis.data('content') || elemCell.text();
                    othis.find('.'+ELEM_EDIT)[0] || othis.append(input);
                    input.focus();
                }
                return;
            }

            //濡傛灉鍑虹幇鐪佺暐锛屽垯鍙煡鐪嬫洿澶�
            if(elemCell.find('.layui-form-switch,.layui-form-checkbox')[0]) return; //闄愬埗涓嶅嚭鐜版洿澶氾紙鏆傛椂锛�

            if(Math.round(elemCell.prop('scrollWidth')) > Math.round(elemCell.outerWidth())){
                that.tipsIndex = layer.tips([
                    '<div class="layui-table-tips-main" style="margin-top: -'+ (elemCell.height() + 16) +'px;'+ function(){
                        if(options.size === 'sm'){
                            return 'padding: 4px 15px; font-size: 12px;';
                        }
                        if(options.size === 'lg'){
                            return 'padding: 14px 15px;';
                        }
                        return '';
                    }() +'">'
                    ,elemCell.html()
                    ,'</div>'
                    ,'<i class="layui-icon layui-table-tips-c">&#x1006;</i>'
                ].join(''), elemCell[0], {
                    tips: [3, '']
                    ,time: -1
                    ,anim: -1
                    ,maxWidth: (device.ios || device.android) ? 300 : 600
                    ,isOutAnim: false
                    ,skin: 'layui-table-tips'
                    ,success: function(layero, index){
                        layero.find('.layui-table-tips-c').on('click', function(){
                            layer.close(index);
                        });
                    }
                });
            }
        });

        //宸ュ叿鏉℃搷浣滀簨浠�
        that.layBody.on('click', '*[lay-event]', function(){
            var othis = $(this)
                ,index = othis.parents('tr').eq(0).data('index')
                ,tr = that.layBody.find('tr[data-index="'+ index +'"]')
                ,ELEM_CLICK = 'layui-table-click'
                ,data = table.cache[that.key][index];

            layui.event.call(this, MOD_NAME, 'tool('+ filter +')', {
                data: table.clearCacheKey(data)
                ,event: othis.attr('lay-event')
                ,tr: tr
                ,del: function(){
                    table.cache[that.key][index] = [];
                    tr.remove();
                    that.scrollPatch();
                }
                ,update: function(fields){
                    fields = fields || {};
                    layui.each(fields, function(key, value){
                        if(key in data){
                            var templet, td = tr.children('td[data-field="'+ key +'"]');
                            data[key] = value;
                            that.eachCols(function(i, item2){
                                if(item2.field == key && item2.templet){
                                    templet = item2.templet;
                                }
                            });
                            td.children(ELEM_CELL).html(
                                templet ? laytpl($(templet).html() || value).render(data) : value
                            );
                            td.data('content', value);
                        }
                    });
                }
            });
            tr.addClass(ELEM_CLICK).siblings('tr').removeClass(ELEM_CLICK);
        });

        //鍚屾婊氬姩鏉�
        that.layMain.on('scroll', function(){
            var othis = $(this)
                ,scrollLeft = othis.scrollLeft()
                ,scrollTop = othis.scrollTop();

            that.layHeader.scrollLeft(scrollLeft);
            that.layFixed.find(ELEM_BODY).scrollTop(scrollTop);

            layer.close(that.tipsIndex);
        });

        _WIN.on('resize', function(){ //鑷€傚簲
            that.fullSize();
            that.scrollPatch();
        });
    };

    //鍒濆鍖�
    table.init = function(filter, settings){
        settings = settings || {};
        var that = this
            ,elemTable = filter ? $('table[lay-filter="'+ filter +'"]') : $(ELEM + '[lay-data]')
            ,errorTips = 'Table element property lay-data configuration item has a syntax error: ';

        //閬嶅巻鏁版嵁琛ㄦ牸
        elemTable.each(function(){
            var othis = $(this), tableData = othis.attr('lay-data');

            try{
                tableData = new Function('return '+ tableData)();
            } catch(e){
                hint.error(errorTips + tableData)
            }

            var cols = [], options = $.extend({
                elem: this
                ,cols: []
                ,data: []
                ,skin: othis.attr('lay-skin') //椋庢牸
                ,size: othis.attr('lay-size') //灏哄
                ,even: typeof othis.attr('lay-even') === 'string' //鍋舵暟琛岃儗鏅�
            }, table.config, settings, tableData);

            filter && othis.hide();

            //鑾峰彇琛ㄥご鏁版嵁
            othis.find('thead>tr').each(function(i){
                options.cols[i] = [];
                $(this).children().each(function(ii){
                    var th = $(this), itemData = th.attr('lay-data');

                    try{
                        itemData = new Function('return '+ itemData)();
                    } catch(e){
                        return hint.error(errorTips + itemData)
                    }

                    var row = $.extend({
                        title: th.text()
                        ,colspan: th.attr('colspan') || 0 //鍒楀崟鍏冩牸
                        ,rowspan: th.attr('rowspan') || 0 //琛屽崟鍏冩牸
                    }, itemData);

                    if(row.colspan < 2) cols.push(row);
                    options.cols[i].push(row);
                });
            });

            //鑾峰彇琛ㄤ綋鏁版嵁
            othis.find('tbody>tr').each(function(i1){
                var tr = $(this), row = {};
                //濡傛灉瀹氫箟浜嗗瓧娈靛悕
                tr.children('td').each(function(i2, item2){
                    var td = $(this)
                        ,field = td.data('field');
                    if(field){
                        return row[field] = td.html();
                    }
                });
                //濡傛灉鏈畾涔夊瓧娈靛悕
                layui.each(cols, function(i3, item3){
                    var td = tr.children('td').eq(i3);
                    row[item3.field] = td.html();
                });
                options.data[i1] = row;
            });
            table.render(options);
        });

        return that;
    };

    //琛ㄦ牸閫変腑鐘舵€�
    table.checkStatus = function(id){
        var nums = 0
            ,invalidNum = 0
            ,arr = []
            ,data = table.cache[id] || [];
        //璁＄畻鍏ㄩ€変釜鏁�
        layui.each(data, function(i, item){
            if(item.constructor === Array){
                invalidNum++; //鏃犳晥鏁版嵁锛屾垨宸插垹闄ょ殑
                return;
            }
            if(item[table.config.checkName]){
                nums++;
                arr.push(table.clearCacheKey(item));
            }
        });
        return {
            data: arr //閫変腑鐨勬暟鎹�
            ,isAll: data.length ? (nums === (data.length - invalidNum)) : false //鏄惁鍏ㄩ€�
        };
    };

    //琛ㄦ牸閲嶈浇
    thisTable.config = {};
    table.reload = function(id, options){
        var config = thisTable.config[id];
        options = options || {};
        if(!config) return hint.error('The ID option was not found in the table instance');
        if(options.data && options.data.constructor === Array) delete config.data;
        return table.render($.extend(true, {}, config, options));
    };

    //鏍稿績鍏ュ彛
    table.render = function(options){
        var inst = new Class(options);
        return thisTable.call(inst);
    };

    //娓呴櫎涓存椂Key
    table.clearCacheKey = function(data){
        data = $.extend({}, data);
        delete data[table.config.checkName];
        delete data[table.config.indexName];
        return data;
    };

    //鑷姩瀹屾垚娓叉煋
    table.init();
    exports(MOD_NAME, table);
});

