
--------------------------------
-- @module IGameSocketSystem
-- @extend IModule
-- @parent_module 

--------------------------------
-- 
-- @function [parent=#IGameSocketSystem] closeSocket 
-- @param self
-- @param #int isocketType
-- @return IGameSocketSystem#IGameSocketSystem self (return value: IGameSocketSystem)
        
--------------------------------
-- 
-- @function [parent=#IGameSocketSystem] connect 
-- @param self
-- @param #char purl
-- @param #int iprot
-- @return int#int ret (return value: int)
        
--------------------------------
-- 
-- @function [parent=#IGameSocketSystem] sendMsg 
-- @param self
-- @param #int isocketType
-- @param #char pdata
-- @param #int isize
-- @return int#int ret (return value: int)
        
--------------------------------
-- 
-- @function [parent=#IGameSocketSystem] releaseInstance 
-- @param self
-- @return IGameSocketSystem#IGameSocketSystem self (return value: IGameSocketSystem)
        
--------------------------------
-- 
-- @function [parent=#IGameSocketSystem] share 
-- @param self
-- @return IGameSocketSystem#IGameSocketSystem ret (return value: IGameSocketSystem)
        
return nil
