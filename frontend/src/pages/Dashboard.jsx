import React from 'react'
import DashboardComponent from '../components/DashboardComponent'
import Header from '../components/Header'
import {FooterComponent} from '../components/FooterComponent'

const Dashboard = () => {
  return (
    <div>
    <Header/>
    <DashboardComponent/>
    <FooterComponent/>
    </div>
  )
}

export default Dashboard
