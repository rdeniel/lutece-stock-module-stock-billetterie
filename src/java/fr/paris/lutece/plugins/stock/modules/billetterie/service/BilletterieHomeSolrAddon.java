/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.stock.modules.billetterie.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.mahout.cf.taste.common.TasteException;

import fr.paris.lutece.plugins.search.solr.service.ISolrSearchAppAddOn;
import fr.paris.lutece.plugins.stock.modules.recommendation.business.RecommendedProduct;
import fr.paris.lutece.plugins.stock.modules.recommendation.service.StockRecommendationService;
import fr.paris.lutece.plugins.stock.modules.tickets.business.ShowDTO;
import fr.paris.lutece.plugins.stock.modules.tickets.service.IShowService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class BilletterieHomeSolrAddon implements ISolrSearchAppAddOn
{
    private static final String ORDER_FILTER_DATE_END = "dateEnd";
    private static final String MARK_SHOW_LIST = "show_list";
    private static final String MARK_TYPE_LIST = "type_list";
    private static final String TYPE_A_LAFFICHE = "aLaffiche";
    private static final String MARK_URL_POSTER = "url_poster";
    private static final String PROPERTY_POSTER_TB_PATH = "stock-billetterie.poster.tb.path";
    public static final String MARK_PRODUCTS_LIST = "products_list";
    private static final String BEAN_STOCK_TICKETS_SHOW_SERVICE = "stock-tickets.showService";

    @Override
    public void buildPageAddOn( Map<String, Object> model, HttpServletRequest request )
    {
        IShowService showServiceHome = (IShowService) SpringContextService.getContext( ).getBean( BEAN_STOCK_TICKETS_SHOW_SERVICE );
        String strUserName = "guid";
        try
        {
            strUserName = getUsername( request );
        }
        catch( UserNotSignedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }

        List<RecommendedProduct> listProducts = null;
        try
        {
            listProducts = StockRecommendationService.instance( ).getRecommendedProducts( strUserName );
        }
        catch( TasteException ex )
        {
            // User not found
            // addError( "User not found" );
            AppLogService.info( "Recommendation error : " + ex.getMessage( ) );
        }

        List<String> orderList = new ArrayList<String>( );
        orderList.add( ORDER_FILTER_DATE_END );

        List<ShowDTO> currentListShow = aLafficheShows( showServiceHome.getCurrentProduct( orderList, null ) );
        // Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_SHOW_LIST, currentListShow );
        model.put( MARK_TYPE_LIST, TYPE_A_LAFFICHE );
        model.put( MARK_URL_POSTER, AppPropertiesService.getProperty( PROPERTY_POSTER_TB_PATH ) );
        model.put( MARK_PRODUCTS_LIST, listProducts );
    }

    /**
     * Gets the user name
     * 
     * @param request
     *            The HTTP request
     * @return the user name
     * @throws UserNotSignedException
     */
    public static String getUsername( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( user == null )
        {
            throw new UserNotSignedException( );
        }

        return user.getName( );
    }

    public List<ShowDTO> aLafficheShows( List<ShowDTO> listShows )
    {
        List<ShowDTO> listShowsReturn = new ArrayList<ShowDTO>( );
        for ( ShowDTO showDTO : listShows )
        {
            if ( showDTO.getAlaffiche( ) )
                listShowsReturn.add( showDTO );
        }

        return listShowsReturn;
    }

}
