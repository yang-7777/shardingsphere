/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.rewrite.token;

import com.google.common.base.Optional;
import org.apache.shardingsphere.core.parse.sql.statement.SQLStatement;
import org.apache.shardingsphere.core.parse.sql.token.SQLToken;
import org.apache.shardingsphere.core.rewrite.token.impl.OrderByTokenGenerator;
import org.apache.shardingsphere.core.rewrite.token.impl.SelectItemsTokenGenerator;
import org.apache.shardingsphere.core.rule.ShardingRule;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * SQL token generator for sharding.
 *
 * @author zhangliang
 */
public final class ShardingTokenGenerateEngine implements SQLTokenGenerateEngine<ShardingRule> {
    
    private static final Collection<SQLTokenGenerator> SQL_TOKEN_GENERATORS = new LinkedList<>();
    
    static {
        SQL_TOKEN_GENERATORS.add(new SelectItemsTokenGenerator());
        SQL_TOKEN_GENERATORS.add(new OrderByTokenGenerator());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<SQLToken> generateSQLTokens(final SQLStatement sqlStatement, final ShardingRule shardingRule) {
        List<SQLToken> result = new LinkedList<>(sqlStatement.getSQLTokens());
        for (SQLTokenGenerator each : SQL_TOKEN_GENERATORS) {
            Optional<? extends SQLToken> sqlToken = each.generateSQLToken(sqlStatement, shardingRule);
            if (sqlToken.isPresent()) {
                result.add(sqlToken.get());
            }
        }
        Collections.sort(result);
        return result;
    }
}