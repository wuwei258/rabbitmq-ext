--- 获取到幂等key
local key = KEYS[1]
local value = KEYS[2]
local expire = ARGV[1]
if redis.call("get",key) == false then
    if redis.call("set",key,value) then
        redis.call("expire",key,tonumber(expire))
    end
    return "true"
else
    return "false"
end

