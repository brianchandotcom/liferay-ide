/*******************************************************************************
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************/
package com.liferay.ide.maven.core;

import java.util.Set;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.osgi.util.NLS;


/**
 * @author Gregory Amerson
 */
public class BuildThumbnailBuildParticipant extends ThemePluginBuildParticipant
{

    @Override
    public Set<IProject> build( int kind, IProgressMonitor monitor ) throws Exception
    {
        IProgressMonitor sub = new SubProgressMonitor( monitor, 100 );

        sub.beginTask( Msgs.thumbnailBuilder, 100 );

        final Set<IProject> retval = super.build( kind, monitor );

        sub.done();

        return retval;
    }

    @Override
    protected void configureExecution( IMavenProjectFacade facade, Xpp3Dom config )
    {
        // dont call super.configure() because we don't need the webappDir
    }

    @Override
    protected String getGoal()
    {
        return ILiferayMavenConstants.PLUGIN_GOAL_BUILD_THUMBNAIL;
    }

    @Override
    protected boolean shouldBuild( int kind, IMavenProjectFacade facade )
    {
        boolean retval = false;

        final IResourceDelta delta = this.getDelta( facade.getProject() );

        //TODO IDE-935 don't hard code path of src/main/webapp/
        if( delta != null && delta.findMember( new Path( "src/main/webapp/images/screenshot.png" ) ) != null ) //$NON-NLS-1$
        {
            retval = true;
        }

        return retval;
    }

    private static class Msgs extends NLS
    {
        public static String thumbnailBuilder;

        static
        {
            initializeMessages( BuildThumbnailBuildParticipant.class.getName(), Msgs.class );
        }
    }
}
