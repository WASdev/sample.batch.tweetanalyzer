/*
 * Copyright 2018 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.websphere.sample.fetcher;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.ibm.websphere.sample.jpa.TweetDataObject;

@Dependent
@Path("tweets")
public class TweetService {
    private final static Logger logger = Logger.getLogger("sample");

    @PersistenceContext(unitName = "tweet-persister")
    EntityManager entityManager;

    List<TweetDataObject> tweets = new ArrayList<TweetDataObject>();

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON })
    public String fetchTweets(@QueryParam("from") Integer startingPoint, @QueryParam("to") Integer endingPoint,
            @QueryParam("orderBy") String category) {

        // hardcoded for now...pagination via front end later
        startingPoint = 0;
        endingPoint = 150;
        String jql;
        if (category.equals("popularity")) {
            jql = "SELECT t FROM TweetDataObject as t ORDER BY t.popularity DESC";
        } else {
            jql = "SELECT t FROM TweetDataObject as t ORDER BY t.creationDate DESC";
        }

        try {
            Query query = entityManager.createQuery(jql);
            query.setFirstResult(startingPoint).setMaxResults(endingPoint);
            tweets = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Something went wrong: " + e);
        }

        String json = new Gson().toJson(tweets);
        return json;
    }
}
